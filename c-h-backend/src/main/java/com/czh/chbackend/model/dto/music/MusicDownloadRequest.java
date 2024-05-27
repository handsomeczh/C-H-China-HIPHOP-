package com.czh.chbackend.model.dto.music;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/24 21:13
 */
@Data
public class MusicDownloadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌曲名")
    private String songName;

    @ApiModelProperty("歌手")
    private String artist;

}
