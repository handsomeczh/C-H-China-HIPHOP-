package com.czh.chbackend.aliyun;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 10:51
 */
@SpringBootTest
class SendSmsUtilsTest {

    @Test
    public void testSend() throws Exception {
        SendSmsUtils.sendMessage("18520282569");
    }

}