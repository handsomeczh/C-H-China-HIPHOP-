package com.czh.chbackend.model.dto.playlist;

import com.czh.chbackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/26 15:23
 */
@Data
public class PlaylistSearchRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private LocalDateTime createTime;

}
