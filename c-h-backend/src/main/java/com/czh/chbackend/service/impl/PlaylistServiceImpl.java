package com.czh.chbackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.mapper.PlaylistMapper;
import com.czh.chbackend.model.dto.playlist.PlaylistSearchRequest;
import com.czh.chbackend.model.entity.Playlist;
import com.czh.chbackend.service.IPlaylistService;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author czh
 * @since 2024-05-26
 */
@Service
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements IPlaylistService {

    @Override
    public Object getPlayLists(PlaylistSearchRequest request) {
        try {
            IPage<Playlist> page = new Page<>(request.getCurrent(), request.getPageSize());
            LambdaQueryWrapper<Playlist> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(request.getName() != null,Playlist::getName,request.getName())
                    .gt(request.getCreateTime() != null, Playlist::getCreateTime,request.getCreateTime())
                    .orderBy(true, true, Playlist::getCreateTime);
            IPage<Playlist> playlistIPage = this.page(page, wrapper);
            return new PageResult(playlistIPage.getTotal(), playlistIPage.getRecords());
        } catch (Exception e) {
            log.error("Error while fetching musics", e);
            return new PageResult(0, null); // 或者抛出自定义异常
        }
    }
}
