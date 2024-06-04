package com.czh.chbackend.controller.music;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.chbackend.aliyun.OssUtil;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.common.Result;
import com.czh.chbackend.model.dto.music.MusicDownloadRequest;
import com.czh.chbackend.model.dto.music.MusicSearchRequest;
import com.czh.chbackend.model.dto.music.MusicUploadRequest;
import com.czh.chbackend.model.entity.Music;
import com.czh.chbackend.model.vo.DownloadMusicVo;
import com.czh.chbackend.model.vo.UrlVo;
import com.czh.chbackend.service.IMusicService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.czh.chbackend.common.ErrorCode.*;
import static com.czh.chbackend.common.CommonConstant.*;

/**
 * 音乐表 前端控制器
 *
 * @author czh
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IMusicService musicService;

    /**
     * 上传音乐（支持歌词上传）
     * todo：目前仅支持音乐上传（MP3格式），支持歌词上传(.lrc格式)
     */
    @PostMapping("/upload")
    public Result uploadMusic(@RequestBody MusicUploadRequest request) {
        String musicPath = request.getMp3FilePath();
        Long musicSize = isMp3File(musicPath);
        if (musicSize.equals(0L)) {
            return Result.error(PARAMS_ERROR, "文件不存在，或者不为MP3格式");
        }
        String artist = request.getArtist();
        String songName = request.getSongName();
        if (artist == null || songName == null) {
            return Result.error(PARAMS_ERROR, "作家和歌曲不能为空");
        }
        // 判断该歌曲是否存在
        boolean exists = musicService.exists(new LambdaQueryWrapper<Music>().eq(Music::getArtist, artist).eq(Music::getSongName, songName));
        if (exists) {
            return Result.error(ALREADY_EXIST);
        }
        // 新增歌曲
        Music newMusic = new Music();
        BeanUtils.copyProperties(request, newMusic);
        newMusic.setMp3FileSize(musicSize);
        // 上传歌曲到OSS
        try {
            OssUtil.upload(musicPath, songName, OSS_MUSIC, OSS_FORMAT_MP3);
            // 上传歌词文件
            String lyricsFilePath = request.getLyricsFilePath();
            Long lrcSize = isLrcFile(lyricsFilePath);
            if (!lrcSize.equals(0L)) {
                newMusic.setLyricsFileSize(lrcSize);
                OssUtil.upload(lyricsFilePath, songName, OSS_WORDS, OSS_FORMAT_LRC);
            }
            musicService.save(newMusic);
        } catch (ClientException e) {
            return Result.error(OPERATION_ERROR, "歌曲上传失败");
        }
        return Result.success();
    }

    /**
     *  下载音乐
     * todo 默认了下载路径，目前暂不支持自定义。默认路径：C:\c_h_music\
     */
    @PostMapping("/download/{id}")
    public Result download(@PathVariable Long id) {
        Music music = musicService.getById(id);
        if(ObjectUtil.isEmpty(music)){
            return Result.error(PARAMS_ERROR,"歌曲不存在");
        }
        // 从OSS下载歌曲
        try {
            OssUtil.downLoad(music.getSongName(), OSS_MUSIC, OSS_FORMAT_MP3);
        } catch (ClientException e) {
            return Result.error(OPERATION_ERROR, "歌曲下载失败");
        }
        return Result.success();
    }

    /**
     * 删除音乐
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        // 查询歌曲是否存在
        Music music = musicService.getById(id);
        if (JSONUtil.isNull(music)) {
            return Result.error(PARAMS_ERROR, "没有该歌曲");
        }
        OssUtil.delete(music.getSongName(), OSS_MUSIC, OSS_FORMAT_MP3);
        // 删除数据库数据
        musicService.removeById(id);
        return Result.success();
    }

    /**
     * 根据歌名、歌手搜索音乐,没有时则全搜索,支持模糊搜索
     * todo 可以实现缓存
     */
    @GetMapping("/search")
    public Result<PageResult> search(@RequestBody MusicSearchRequest request) {
        return Result.success(musicService.getMusicList(request));
    }

    /**
     * 获取音乐的临时URL：用于在线播放,支持显示歌词,返回
     */
    @GetMapping("/url/{id}")
    public Result<UrlVo> getUrl(@PathVariable Long id) {
        // 查询歌曲是否存在
        Music music = musicService.getById(id);
        if (JSONUtil.isNull(music)) {
            return Result.error(PARAMS_ERROR, "歌曲不存在");
        }

        UrlVo urlVo = new UrlVo();
        urlVo.setId(id);

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String songName = music.getSongName();

        String musicKey = REDIS_MUSIC_KEY + id;
        String wordKey = REDIS_WORDS_KEY + id;

        // 先从Redis缓存中获取
        String musicUrl = ops.get(musicKey);
        String wordUrl = ops.get(wordKey);
        if (!StringUtil.isNullOrEmpty(musicUrl)) {
            urlVo.setMusicUrl(musicUrl);
            if (!StringUtil.isNullOrEmpty(wordUrl)) {
                urlVo.setWordUrl(wordUrl);
            }
            return Result.success(urlVo);
        }

        // 缓存不存在生成url
        musicUrl = OssUtil.generatePresignedUrl(songName, OSS_MUSIC, OSS_FORMAT_MP3);
        music.setMp3FilePath(musicUrl);
        urlVo.setMusicUrl(musicUrl);

        // 更新数据库信息和Redis
        ops.set(musicKey, musicUrl);
        redisTemplate.expire(musicKey, 1, TimeUnit.HOURS);

        if (!StringUtil.isNullOrEmpty(music.getLyricsFilePath())) {
            wordUrl = OssUtil.generatePresignedUrl(songName, OSS_WORDS, OSS_FORMAT_LRC);
            music.setLyricsFilePath(wordUrl);
            urlVo.setWordUrl(wordUrl);

            // 更新数据库信息和Redis
            ops.set(wordKey, wordUrl);
            redisTemplate.expire(wordKey, 1, TimeUnit.HOURS);
        }

        musicService.updateById(music);
        return Result.success(urlVo);
    }

    /**
     * 分享下载音乐
     * 和获取音乐的临时URL共用缓存
     * todo 点看该url会直接下载音乐，后期可以实现页面跳转并加载该url
     */
    @GetMapping("/share/{id}")
    public Result<String> share(@PathVariable Long id) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String musicKey = REDIS_MUSIC_KEY + id;
        // 从Redis缓存中获取
        String musicUrl = ops.get(musicKey);
        if (musicUrl != null) {
            return Result.success(musicUrl);
        }
        // 从数据库获取，并判断是否过期
        // 查询歌曲是否存在
        Music music = musicService.getById(id);
        if (JSONUtil.isNull(music)) {
            return Result.error(PARAMS_ERROR, "歌曲不存在");
        }
        musicUrl = music.getMp3FilePath();
        String songName = music.getSongName();
        if (!OssUtil.isUrlExpired(musicUrl)) {
            return Result.success(musicUrl);
        }
        // 过期重新生成新的url并更新
        musicUrl = OssUtil.generatePresignedUrl(songName, OSS_MUSIC, OSS_FORMAT_MP3);
        music.setMp3FilePath(musicUrl);

        // 更新数据库信息和Redis
        ops.set(musicKey, musicUrl);
        redisTemplate.expire(musicKey, 1, TimeUnit.HOURS);

        if (!StringUtil.isNullOrEmpty(music.getLyricsFilePath())) {
            String wordUrl = OssUtil.generatePresignedUrl(songName, OSS_WORDS, OSS_FORMAT_LRC);
            music.setLyricsFilePath(wordUrl);

            // 更新数据库信息和Redis
            String wordKey = REDIS_WORDS_KEY + id;
            ops.set(wordKey, wordUrl);
            redisTemplate.expire(wordKey, 1, TimeUnit.HOURS);
        }
        //放回url
        return Result.success(musicUrl);
    }

    /**
     * 已下载音乐
     * 默认读取本地缓存 C:\c_h_music\
     * todo 暂且不使用Redis缓存，必要性不高
     */
    @GetMapping("/downloaded")
    public Result<List<DownloadMusicVo>> downloaded(String path){
        if(StrUtil.isBlank(path)){
            path = DOWNLOAD_MUSIC_DIRECTORY;
        }
        // 创建一个集合存储音乐文件路径 DownLoadMusicVo todo 为解决显示问题(图片，歌名等)
        ArrayList<DownloadMusicVo> vos = new ArrayList<>();
        // 读取指定目录下的 .mp3 文件
        File directory = new File(DOWNLOAD_MUSIC_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (files != null) {
                for (File file : files) {
                    vos.add(DownloadMusicVo.builder()
                            .songName(file.getName())
                            .path(file.getAbsolutePath())
                            .build());
                }
            }
        } else {
            return Result.error(NOT_FOUND_ERROR,"本地文件C:\\c_h_music\\不存在！");
        }
        return Result.success(vos);
    }

    /**
     * todo 热歌推荐
     */
    @PostMapping("/hot")
    public Result<List> hot(){
        return Result.success();
    }

    // region 私有方法 isMp3File isLrcFile

    /**
     * 判断路径文件是否存在，并且是否为MP3格式
     *
     * @param filePath 文件路径
     * @return 如果文件存在且为MP3格式，返回true；否则返回false
     */
    private static Long isMp3File(String filePath) {
        File file = new File(filePath);

        // 判断文件是否存在
        if (!file.exists() || !file.isFile()) {
            return 0L;
        }

        // 判断文件是否为MP3格式
        String fileName = file.getName();
        if (!fileName.toLowerCase().endsWith(".mp3")) {
            return 0L;
        }

        return file.length();
    }

    /**
     * 判断路径文件是否存在，并且是否为LRC格式
     * 如果文件存在且为LRC格式，返回文件大小；否则返回0表示文件不存在或不是LRC格式
     *
     * @param filePath 文件路径
     * @return 文件大小（字节数），如果文件不存在或不是LRC格式，返回0
     */
    private static Long isLrcFile(String filePath) {
        File file = new File(filePath);

        // 判断文件是否存在
        if (!file.exists() || !file.isFile()) {
            return 0L;
        }

        // 判断文件是否为LRC格式
        String fileName = file.getName();
        if (!fileName.toLowerCase().endsWith(".lrc")) {
            return 0L;
        }

        // 返回文件大小
        return file.length();
    }

    // endregion
}
