package com.czh.chbackend.model.dto.playlist;

import com.czh.chbackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 21:52
 */
@Data
public class PlaylistSongGetRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long playlistId;
}
