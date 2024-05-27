package com.czh.chbackend.model.dto.playlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 14:50
 */
@Data
public class PlaylistUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String listImage;

    private String name;

    private String description;
}
