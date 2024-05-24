package com.czh.chbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/21 19:25
 */
@Data
public class Result<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Result(int code, T data) {
        this(code, data, "");
    }

    public Result(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), data, "ok");
    }

    public static  Result success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), null, "ok");
    }

    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public static Result error(int code, String message) {
        return new Result(code, message);
    }

    public static Result error(ErrorCode errorCode, String message) {
        return new Result(errorCode.getCode(), null, message);
    }
}
