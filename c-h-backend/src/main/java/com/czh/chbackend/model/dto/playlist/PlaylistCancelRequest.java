package com.czh.chbackend.model.dto.playlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 23:53
 */
@Data
public class PlaylistCancelRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long playlistId;

    private Long songId;
}
