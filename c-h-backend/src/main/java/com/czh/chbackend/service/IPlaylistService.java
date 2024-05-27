package com.czh.chbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.chbackend.model.dto.playlist.PlaylistSearchRequest;
import com.czh.chbackend.model.entity.Playlist;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author czh
 * @since 2024-05-26
 */
public interface IPlaylistService extends IService<Playlist> {

    Object getPlayLists(PlaylistSearchRequest request);
}
