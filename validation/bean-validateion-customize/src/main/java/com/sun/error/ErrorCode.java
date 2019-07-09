package com.sun.error;


/**
 *
 */
public enum ErrorCode {


    NOT_NULL(10111, "不能为空");


    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}