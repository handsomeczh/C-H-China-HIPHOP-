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
}
