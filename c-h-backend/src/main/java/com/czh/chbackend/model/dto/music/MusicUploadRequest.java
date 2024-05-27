package com.czh.chbackend.model.dto.music;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/24 21:13
 */
@Data
public class MusicUploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("MP3文件大小，字节为单位")
    private Long mp3FileSize;

    @ApiModelProperty("歌词文件在OSS存储系统中的路径")
    private String lyricsFilePath;

    @ApiModelProperty("歌词文件大小，字节为单位")
    private Long lyricsFileSize;

}
