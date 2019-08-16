package com.zhangwx.exception;

public class BaseException extends RuntimeException{

    private Integer code;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Integer code,String message) {
        super(message);
        this.code=code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
