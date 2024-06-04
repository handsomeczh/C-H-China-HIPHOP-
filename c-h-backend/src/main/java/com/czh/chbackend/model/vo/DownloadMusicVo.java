package com.czh.chbackend.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 已下载列表对象
 * @author czh
 * @version 1.0.0
 * 2024/6/3 9:41
 */
@Data
@Builder
public class DownloadMusicVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long playlistId;

    private String path;

    private String songName;

    private String artist;

    private String songImage;
}