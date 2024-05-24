package com.czh.chbackend.utils;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 22:10
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    // 加密方法，使用SHA-256进行加密（不带盐值）
    public static String encryptWithoutSalt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // 加密方法，自动生成盐值并返回加密后的密码和盐值（带盐值）
    public static String[] encryptWithSalt(String password) {
        String salt = generateSalt();
        String encryptedPassword = encryptWithSalt(password, salt);
        return new String[]{encryptedPassword, salt};
    }

    // 使用给定的盐值加密密码
    private static String encryptWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // 生成盐值
    private static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 验证密码方法（不带盐值）
    public static boolean verifyWithoutSalt(String password, String encryptedPassword) {
        return encryptWithoutSalt(password).equals(encryptedPassword);
    }

    // 验证密码方法（带盐值）
    public static boolean verifyWithSalt(String password, String encryptedPassword, String salt) {
        return encryptWithSalt(password, salt).equals(encryptedPassword);
    }

}
