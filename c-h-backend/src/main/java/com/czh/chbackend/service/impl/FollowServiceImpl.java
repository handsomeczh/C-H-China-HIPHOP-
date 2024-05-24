package com.czh.chbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.chbackend.common.PageRequest;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.mapper.FollowMapper;
import com.czh.chbackend.model.entity.Follow;
import com.czh.chbackend.service.IFollowService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/23 20:30
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

    @Override
    public PageResult getFollow(PageRequest pageRequest, Long fanId) {
        try {
            IPage<Follow> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
            LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Follow::getFanId, fanId).orderBy(true, true, Follow::getCreateTime);
            IPage<Follow> followIPage = this.page(page, wrapper);
            return new PageResult(followIPage.getTotal(), followIPage.getRecords());
        } catch (Exception e) {
            logger.error("Error while fetching follows", e);
            return new PageResult(0, null); // 或者抛出自定义异常
        }
    }

    @Override
    public PageResult getFan(PageRequest pageRequest, Long followId) {
        try {
            IPage<Follow> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
            LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Follow::getFollowId, followId).orderBy(true, true, Follow::getCreateTime);
            IPage<Follow> followIPage = this.page(page, wrapper);
            return new PageResult(followIPage.getTotal(), followIPage.getRecords());
        } catch (Exception e) {
            logger.error("Error while fetching fans", e);
            return new PageResult(0, null); // 或者抛出自定义异常
        }
    }
}
