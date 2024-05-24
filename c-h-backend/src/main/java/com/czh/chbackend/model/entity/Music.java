package com.czh.chbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 音乐表
 * </p>
 *
 * @author czh
 * @since 2024-05-24
 */
@Data
@ApiModel(value = "Music对象", description = "音乐表")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("歌曲名")
    private String songName;

    @ApiModelProperty("歌手")
    private String artist;

    @ApiModelProperty("专辑名称")
    private String album;

    @ApiModelProperty("发行时间")
    private LocalDate releaseDate;

    @ApiModelProperty("音乐类型")
    private String genre;

    @ApiModelProperty("语种")
    private String language;

    @ApiModelProperty("歌曲图片")
    private String songImagePath;

    @ApiModelProperty("MP3文件在OSS存储系统中的路径")
    private String mp3FilePath;

    @ApiModelProperty("歌词文件在OSS存储系统中的路径")
    private String lyricsFilePath;

    @ApiModelProperty("MP3文件大小，字节为单位")
    private Long mp3FileSize;

    @ApiModelProperty("歌词文件大小，字节为单位")
    private Long lyricsFileSize;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

}
