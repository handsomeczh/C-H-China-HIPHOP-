package com.czh.chbackend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.common.Result;
import com.czh.chbackend.model.dto.playlist.*;
import com.czh.chbackend.model.entity.Music;
import com.czh.chbackend.model.entity.Playlist;
import com.czh.chbackend.model.entity.PlaylistSong;
import com.czh.chbackend.model.vo.FanOrFollowVo;
import com.czh.chbackend.service.IMusicService;
import com.czh.chbackend.service.IPlaylistService;
import com.czh.chbackend.service.IPlaylistSongService;
import com.czh.chbackend.utils.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.czh.chbackend.common.CommonConstant.REDIS_PLAYLIST;
import static com.czh.chbackend.common.CommonConstant.REDIS_COLLECTION_PLAYLIST;
import static com.czh.chbackend.common.ErrorCode.*;

/**
 * 歌单相关接口
 *
 * @author czh
 * @since 2024-05-26
 */
@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IPlaylistSongService playlistSongService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IMusicService musicService;

    /**
     * 创建歌单
     * todo 可以将图片上传至阿里云保存
     */
    @PostMapping("/create")
    public Result create(@RequestBody PlaylistCreateRequest request) {
        // 歌单name不能重复
        boolean exists = playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getName, request.getName()));
        if (exists) {
            return Result.error(ALREADY_EXIST, "歌单存在");
        }
        // 更新歌单
        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(request, playlist);
        playlist.setUserId(UserContext.getCurrentId());
        playlistService.save(playlist);
        return Result.success();
    }

    /**
     * 删除歌单
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean exists = playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getId, id));
        if (!exists) {
            return Result.error(ALREADY_EXIST, "歌单不存在");
        }
        playlistService.removeById(id);
        return Result.success();
    }

    /**
     * 更新歌单
     * 前端需要传递全信息
     */
    @PostMapping("/update")
    public Result update(@RequestBody PlaylistUpdateRequest request) {
        // 歌单name不能重复
        boolean exists = playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getName, request.getName()));
        if (exists) {
            return Result.error(ALREADY_EXIST, "歌单存在");
        }
        // 歌单判断
        Playlist playlist = isCollectionList(request.getId());
        if (JSONUtil.isNull(playlist)) {
            return Result.error(OPERATION_ERROR, "不能更新收藏歌单");
        }

        BeanUtil.copyProperties(request, playlist);
        playlist.setUserId(UserContext.getCurrentId());
        playlistService.updateById(playlist);
        return Result.success();
    }

    /**
     * 查询歌单
     */
    @GetMapping("/search")
    public Result search(@RequestBody PlaylistSearchRequest request) {
        return Result.success(playlistService.getPlayLists(request));
    }

    /**
     * 收藏歌曲/添加歌曲进歌单
     */
    @PostMapping("/add")
    public Result add(@RequestBody PlaylistAddRequest request) {
        // 判断歌单和歌曲是否存在
        if (!playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getId, request.getPlaylistId()))
                || !musicService.exists(new LambdaQueryWrapper<Music>().eq(Music::getId, request.getSongId()))) {
            return Result.error(PARAMS_ERROR, "歌单或歌曲不存在");
        }
        // 判断是否已经添加
        if (
                playlistSongService.exists(new LambdaQueryWrapper<PlaylistSong>().eq(PlaylistSong::getPlaylistId, request.getPlaylistId())
                        .eq(PlaylistSong::getSongId, request.getSongId()))){
            return Result.error(ALREADY_EXIST,"歌曲也存在");
        }
        PlaylistSong playlistSong = new PlaylistSong();
        BeanUtil.copyProperties(request, playlistSong);
        playlistSongService.save(playlistSong);
        // 删除Redis缓存
        String key = REDIS_PLAYLIST + ":" + request.getPlaylistId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 取消收藏歌曲/取消添加进歌单歌曲
     */
    @DeleteMapping("/cancel")
    public Result cancelSong(@RequestBody PlaylistCancelRequest request) {
        // 判断歌单和歌曲是否存在
        if (!playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getId, request.getPlaylistId()))
                || !musicService.exists(new LambdaQueryWrapper<Music>().eq(Music::getId, request.getSongId()))) {
            return Result.error(PARAMS_ERROR, "歌单或歌曲不存在");
        }
        playlistSongService.remove(new LambdaQueryWrapper<PlaylistSong>()
                .eq(PlaylistSong::getPlaylistId, request.getPlaylistId())
                .eq(PlaylistSong::getSongId, request.getSongId())
        );
        // 删除Redis缓存
        String key = REDIS_PLAYLIST + ":" + request.getPlaylistId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 获取歌单歌曲数据
     */
    @GetMapping("/playlistSong")
    public Result<PageResult> getListSongs(@RequestBody PlaylistSongGetRequest request) {
        if (!playlistService.exists(new LambdaQueryWrapper<Playlist>().eq(Playlist::getId, request.getPlaylistId()))) {
            return Result.error(PARAMS_ERROR, "歌单或歌曲不存在");
        }
        // 获取歌单歌曲(Redis)
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        String key = REDIS_PLAYLIST + ":" + request.getPlaylistId();

        List<String> lists = ops.randomMembers(key, ops.size(key));
        if (lists != null && lists.size() > 0) {
            List<PlaylistSong> listSongs = lists.stream().map(obj -> JSONUtil.toBean(obj, PlaylistSong.class)).collect(Collectors.toList());
            return Result.success(new PageResult(listSongs.size(), listSongs));
        }

        // 获取歌单歌曲(数据库)
        List<PlaylistSong> listSongs = playlistSongService.list(new Page<>(request.getCurrent(), request.getPageSize())
                , new LambdaQueryWrapper<PlaylistSong>()
                        .eq(PlaylistSong::getPlaylistId, request.getPlaylistId()));
        // 存入Redis缓存
        for (PlaylistSong song : listSongs) {
            ops.add(key, JSONUtil.toJsonStr(song));
        }
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
        return Result.success(new PageResult(listSongs.size(), listSongs));
    }

    /**
     * 判断是不是收藏歌单,并返回歌单对象
     * 收藏歌单命名：收藏+userId
     * 是返回歌单对象，不是返回null；
     */
    private Playlist isCollectionList(Long id) {
        Playlist playlist = playlistService.getById(id);
        if (JSONUtil.isNull(playlist) || playlist.getName().equals(REDIS_COLLECTION_PLAYLIST + UserContext.getCurrentId())) {
            return null;
        }
        return playlist;
    }

}
