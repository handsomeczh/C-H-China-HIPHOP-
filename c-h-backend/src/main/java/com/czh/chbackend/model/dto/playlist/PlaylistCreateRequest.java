package com.czh.chbackend.model.dto.playlist;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 14:37
 */
@Data
public class PlaylistCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String listImage;

    private String name;

    private String description;
}
