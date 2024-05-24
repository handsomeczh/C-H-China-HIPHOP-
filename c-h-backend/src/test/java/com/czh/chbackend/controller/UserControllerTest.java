package com.czh.chbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czh.chbackend.common.PageRequest;
import com.czh.chbackend.common.PageResult;
import com.czh.chbackend.common.Result;
import com.czh.chbackend.model.entity.Follow;
import com.czh.chbackend.model.vo.FanOrFollowVo;
import com.czh.chbackend.service.IFollowService;
import com.czh.chbackend.utils.UserContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.czh.chbackend.constant.CommonConstant.REDIS_FOLLOW_KEY;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/24 10:12
 */
@SpringBootTest
class UserControllerTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IFollowService followService;

    @Test
    public void test(){
        Long userId = 2L;
        // key
        String key = REDIS_FOLLOW_KEY+userId;
        HashOperations<String, Object, Object> forHash = redisTemplate.opsForHash();
        // 从Redis中获取数据
        List<Object> list = forHash.values(key);
        if(list != null && !list.isEmpty()){
            System.out.println("123456789");
        }
        // 缓存不存在从数据库获取
        PageRequest pageRequest = new PageRequest();
        PageResult pageResult = followService.getFollow(pageRequest,userId);
        List<Follow> records = pageResult.getRecords();
        // 数据处理
        List<FanOrFollowVo> follows = records.stream().map(entity -> {
                    FanOrFollowVo vo = new FanOrFollowVo();
                    vo.setId(entity.getFollowId());
                    vo.setUserName(entity.getFollowName());
                    vo.setUserAvatar(entity.getFollowAvatar());
                    // 存入Redis
                    forHash.put(key,String.valueOf(entity.getFollowId()), vo);
                    return vo;
                })
                .collect(Collectors.toList());

        // 设置Redis键的过期时间为2小时
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
        pageResult.setRecords(follows);
        System.out.println("success");
    }

}