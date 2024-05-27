package com.czh.chbackend.model.dto.music;

import com.czh.chbackend.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/25 11:57
 */
@Data
public class MusicSearchRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌曲名")
    private String songName;

    @ApiModelProperty("歌手")
    private String artist;

}
