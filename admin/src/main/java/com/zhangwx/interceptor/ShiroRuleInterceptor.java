package com.zhangwx.interceptor;

import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.enums.EnumSysRole;
import com.zhangwx.exception.ServiceException;
import com.zhangwx.model.SysRole;
import com.zhangwx.service.SysUserService;
import com.zhangwx.util.UserRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 拦截器，
 */
public class ShiroRuleInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private SysUserService sysUserService;


}
