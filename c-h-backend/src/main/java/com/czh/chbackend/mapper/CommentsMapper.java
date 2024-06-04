package com.czh.chbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.chbackend.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author czh
 * @since 2024-05-27
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comment> {

}
