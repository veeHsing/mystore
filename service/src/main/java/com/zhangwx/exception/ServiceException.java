package com.zhangwx.exception;

import com.zhangwx.constants.MyExceptionCode;
import org.springframework.stereotype.Component;

@Component
public class ServiceException extends BaseException {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(MyExceptionCode e){
        super(e.getCode(),e.getMsg());
    }

    public ServiceException(Integer code, String message) {
        super(code, message);
    }
}
