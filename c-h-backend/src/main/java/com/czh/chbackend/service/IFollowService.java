package com.czh.chbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.chbackend.common.PageRequest;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.model.entity.Follow;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/23 20:30
 */
public interface IFollowService extends IService<Follow> {
    PageResult getFollow(PageRequest pageRequest, Long userId);

    PageResult getFan(PageRequest pageRequest, Long userId);
}
