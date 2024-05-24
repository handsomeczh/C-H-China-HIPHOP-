package com.czh.chbackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.chbackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author czh
 * @since 2024-05-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
