package com.zhangwx.util;

import com.zhangwx.constants.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * request请求中带着用户信息
 */
public class UserRequest {

    public static long getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JJWTUtil.JWTBody jwtBody=(JJWTUtil.JWTBody)request.getAttribute(Constants.JWT_BODY);
        return jwtBody.getUserId();
    }

    public static long getCurrentRoleId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JJWTUtil.JWTBody jwtBody=(JJWTUtil.JWTBody)request.getAttribute(Constants.JWT_BODY);
        return jwtBody.getRoleId();
    }


}
