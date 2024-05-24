package com.czh.chbackend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 22:13
 */
class PasswordUtilTest {

    @Test
    public void test(){
        String password = "12345678";

        System.out.println(PasswordUtil.encryptWithoutSalt(password));
    }

}