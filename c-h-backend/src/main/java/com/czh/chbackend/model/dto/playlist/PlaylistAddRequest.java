package com.czh.chbackend.model.dto.playlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 21:41
 */
@Data
public class PlaylistAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long playlistId;

    private Long songId;

    private String songName;

    private String artist;

    private String songImage;
}
