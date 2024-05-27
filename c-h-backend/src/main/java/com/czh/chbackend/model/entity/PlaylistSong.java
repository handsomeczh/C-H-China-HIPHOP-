package com.czh.chbackend.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @since 2024-05-26
 */
@Data
@ApiModel(value = "PlaylistSong对象", description = "歌单歌曲")
public class PlaylistSong implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long playlistId;

    private Long songId;

    private String songName;

    private String artist;

    private String songImage;

}
