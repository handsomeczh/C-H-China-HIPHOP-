package com.czh.chbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.chbackend.model.entity.Music;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 音乐表 Mapper 接口
 * </p>
 *
 * @author czh
 * @since 2024-05-24
 */
@Mapper
public interface MusicMapper extends BaseMapper<Music> {

}
