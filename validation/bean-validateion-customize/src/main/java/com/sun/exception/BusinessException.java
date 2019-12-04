package com.sun.exception;

import org.springframework.util.Assert;

/**
 * @author
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    @Deprecated
    public BusinessException() {

    }

    @Deprecated
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Deprecated
    public BusinessException(Throwable throwable, ErrorCode errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    @Deprecated
    public BusinessException(String message) {
        this.errorCode = new ErrorCode(message);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * 自定义异常新创建方式
     *
     * @param errorCode
     * @return
     */
    public static BusinessException build(ErrorCode errorCode) {
        Assert.notNull(errorCode.getCode(), "errorCode 不能为空");

        return new BusinessException(errorCode);
    }

    public static BusinessException build(ErrorCode errorCode, Object... params) {
        Assert.notNull(errorCode.getCode(), "errorCode 不能为空");
        Assert.notNull(errorCode.getMessage(), "errorCode.message 不能为空");

        String format = String.format(errorCode.getMessage(), params);
        errorCode.setMessage(format);

        return build(errorCode.getCode(), errorCode.getMessage());
    }

    public static BusinessException build(int code, String message) {
        ErrorCode errorCode = new ErrorCode(code, message);
        return build(errorCode);
    }

}