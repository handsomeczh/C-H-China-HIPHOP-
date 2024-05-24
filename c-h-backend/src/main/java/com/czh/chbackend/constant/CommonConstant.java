package com.czh.chbackend.constant;

import java.util.regex.Pattern;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/22 13:29
 */
public class CommonConstant {

    // 电话号
    private static final String PHONE_NUMBER_REGEX = "^1[3-9]\\d{9}$";
    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    // 排序
    /**
     * 升序
     */
    public static final String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    public static final String SORT_ORDER_DESC = " descend";

    //Redis键常量
    public static final String REDIS_FOLLOW_KEY = "follows:";
    public static final String REDIS_FAN_KEY = "fan:";


    //OSS存储
    public static final String OSS_MUSIC = "music";
    public static final String OSS_WORDS = "words";

    public static final String OSS_FORMAT_MP3 = ".mp3";
    public static final String OSS_FORMAT_TXT = ".txt";

    // 文件保存地址
    public static final String LOCAL_SAVE_ADDRESS = "C:\\Desktop\\";
}
