package com.czh.chbackend.common;

import lombok.Getter;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/21 19:33
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "ok"),
    ERROR_CODE(20200,"重复操作"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NOT_REGISTER_ERROR(40102, "未注册"),
    PASSWORD_ERROR(40104, "密码错误"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
