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

public class ShiroRuleInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("++++++++++++="+sysUserService);
        SysRole sysRole=sysUserService.selectSysRoleByPrimaryKey(UserRequest.getCurrentRoleId());
        Optional.ofNullable(sysRole).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_ROLE_NOT_EXIST));
        if (sysRole.getDeleted() == EnumSysRole.DELETED_NO.getCode()){
            throw new ServiceException(MyExceptionCode.SYS_DENY);
        }
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole(sysRole.getCode())) {
            // 有权限，执行相关业务
            return true;
        } else {
            // 无权限，给相关提示
            throw new ServiceException(MyExceptionCode.SYS_DENY);
//            return false;
        }
    }



}
