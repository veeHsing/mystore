package com.zhangwx.shiro;

import com.zhangwx.base.Result;
import com.zhangwx.constants.Constants;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.core.ResponseResult;
import com.zhangwx.core.JWTToken;
import com.zhangwx.service.SysUserService;
import com.zhangwx.util.JJWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截url，处理jwt
public class JJWTFilter extends BasicHttpAuthenticationFilter {

    // 登录标识
    private static String LOGIN_SIGN = "Authorization";

    @Autowired
    private SysUserService sysUserService;

    /**
     * 检测用户是否登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(LOGIN_SIGN);
        return authorization != null;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                //如果存在token，执行登录
                boolean is_login = executeLogin(request, response);
                if (!is_login) {
                    throw new Exception("jwtToken解析失败！");
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Result result = new Result(MyExceptionCode.SYS_JWT_PARSE_FAIL);
                ResponseResult.responseResult(response, result);
                return false;
            }
        } else {
            Result result = new Result(MyExceptionCode.SYS_JWT_CANT_NULL);
            ResponseResult.responseResult(response, result);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return super.onAccessDenied(request, response);
    }

    //登录，验证token
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String token = req.getHeader(LOGIN_SIGN);
            if (token.substring(0, 6).equalsIgnoreCase("bearer")) {
                token = token.substring(6);
            }
            token = token.trim();
            JJWTUtil.JWTBody jwtBody = JJWTUtil.getBody(token);
            if (jwtBody == null) {
                return false;
            }
            JWTToken jwtToken = new JWTToken(jwtBody.getUserName(), token);
            Subject subject = getSubject(request, response);
            subject.login(jwtToken);
            HttpServletRequest request1 = (HttpServletRequest) request;
            request1.setAttribute(Constants.JWT_BODY, jwtBody);
            return true;
        } catch (AuthenticationException e) {
            //抛出异常，解析失败
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
