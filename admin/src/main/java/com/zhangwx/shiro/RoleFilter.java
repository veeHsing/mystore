package com.zhangwx.shiro;

import com.zhangwx.base.Result;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.core.ResponseResult;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 权限判断
 */
public class RoleFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String[] arr = (String[]) mappedValue;
        Subject subject = getSubject(request, response);
        for (String role : arr) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        //if false 跳到onAccessDenied
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Result result = new Result(MyExceptionCode.SYS_DENY);
        ResponseResult.responseResult(response, result);
        return false;
    }
}
