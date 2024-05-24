package com.czh.chbackend.interceptor;

import com.czh.chbackend.constant.CommonConstant;
import com.czh.chbackend.utils.JwtUtil;
import com.czh.chbackend.utils.UserContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null|| !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            token = token.substring(7); // Remove "Bearer " prefi
            UserContext.setCurrentId(JwtUtil.getUserIdFromToken(token));
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除Redis缓存
        String followKey = CommonConstant.REDIS_FOLLOW_KEY+UserContext.getCurrentId();
        redisTemplate.delete(followKey);

        String fanKay = CommonConstant.REDIS_FAN_KEY+UserContext.getCurrentId();
        redisTemplate.delete(fanKay);

        UserContext.removeCurrentId();
    }
}
