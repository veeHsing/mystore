package com.zhangwx.handler;

import com.zhangwx.base.Result;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.exception.ServiceException;
import com.zhangwx.util.ResultsUtil;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一全局拦截异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    private static  Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Result handleException(Exception e){
        logger.info("=============正在处理全局处理异常========");
        e.printStackTrace();
        if (e instanceof ServiceException){
            ServiceException serviceException=(ServiceException) e;
            return  ResultsUtil.failure(serviceException.getCode(),serviceException.getMessage());
        }else if (e instanceof MethodArgumentNotValidException){
            List<String> errorInformation=((MethodArgumentNotValidException) e).getBindingResult().getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResultsUtil.failure(-1,errorInformation.toString());
        }else if(e instanceof AuthorizationException || e instanceof UnauthorizedException){
            return ResultsUtil.failure(MyExceptionCode.SYS_DENY);
        }else if (e instanceof HttpMessageNotReadableException){
            return ResultsUtil.failure(MyExceptionCode.SYS_HTTP_MESSAGE);
        }else if(e instanceof UnavailableSecurityManagerException){
            return ResultsUtil.failure(MyExceptionCode.SYS_SHIRO_INVALID_URL);
        }
        else {
            logger.error("系统异常=》",e.getMessage());
            return ResultsUtil.failure(MyExceptionCode.SYS_EXCEPTION);
        }

    }


}
