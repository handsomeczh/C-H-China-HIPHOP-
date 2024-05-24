package com.czh.chbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 24小时
    private static final Long EXPIRATION_TIME_MILLIS = 86400000L;

    // 生成JWT
    public static String generateToken(Long userId, String phoneNumber) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MILLIS);

        JwtBuilder builder = Jwts.builder()
                .setSubject("user-authentication") // 设置主题
                .claim("userId", userId) // 设置用户id
                .claim("phoneNumber", phoneNumber) // 设置手机号
                .setIssuedAt(now) // 设置JWT的签发时间
                .setExpiration(expiration) // 设置JWT的过期时间
                .signWith(key); // 设置签名使用的算法和秘钥

        return builder.compact();
    }

    // 解析JWT并获取payload内容
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取JWT中的用户id
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Number userId = (Number) claims.get("userId");
        return userId.longValue();
    }

    // 获取JWT中的手机号
    public static String getPhoneNumberFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("phoneNumber");
    }

    // 可以添加其他方法，如验证JWT的有效性等

}
