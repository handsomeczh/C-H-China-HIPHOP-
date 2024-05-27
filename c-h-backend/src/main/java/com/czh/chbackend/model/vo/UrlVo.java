package com.czh.chbackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/25 14:25
 */
@Data
public class UrlVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("歌曲id")
    private Long id;

    @ApiModelProperty("musicUrl")
    private String musicUrl;

    @ApiModelProperty("歌词文件Url")
    private String wordUrl;

}
