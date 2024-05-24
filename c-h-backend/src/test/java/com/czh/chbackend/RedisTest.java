package com.czh.chbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 14:31
 */
@SpringBootTest
class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("18520282538","123456",2L, TimeUnit.MINUTES);
    }
}
