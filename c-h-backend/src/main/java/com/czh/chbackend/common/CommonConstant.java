package com.czh.chbackend.common;

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

    public static final String REDIS_MUSIC_KEY = "music:";
    public static final String REDIS_WORDS_KEY = "words:";


    //OSS存储
    public static final String OSS_MUSIC = "music";
    public static final String OSS_WORDS = "words";

    public static final String OSS_FORMAT_MP3 = ".mp3";
    public static final String OSS_FORMAT_LRC = ".lrc";

    // 文件保存地址
    public static final String LOCAL_SAVE_ADDRESS = "C:\\c_h_music\\";

    // 歌单
    public static final String REDIS_COLLECTION_PLAYLIST = "收藏";
    public static final String REDIS_DOWNLOAD_PLAYLIST = "下载";
    public static final String REDIS_PLAYLIST = "歌单";

    // 热门评论
    public static final String REDIS_HOT_COMMENTS = "hot_comments:";
    public static final String REDIS_NEW_COMMENTS = "new_comments:";

    // 回复
    public static final String REDIS_REPLY_KEY = "reply:";

    // 默认本地下载地址
    public static final String DOWNLOAD_MUSIC_DIRECTORY = "C:\\c_h_music\\";
}
