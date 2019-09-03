package com.zhangwx.shiro;

import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.core.JWTToken;
import com.zhangwx.enums.EnumSysUser;
import com.zhangwx.exception.ServiceException;
import com.zhangwx.model.SysRole;
import com.zhangwx.model.SysUser;
import com.zhangwx.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Shiro核心配置
 */
public class MyShiroRealm extends AuthorizingRealm {

    private static final Logger logger= LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private SysUserService sysUserService;

    private boolean cachingEnabled=false;

    /**
     * 授权认证
     * 权限信息，包括角色以及权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username=principals.toString();
        SysUser sysUser=sysUserService.selectByUsername(username);
        Optional.ofNullable(sysUser).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST));
        SysRole sysRole=sysUserService.selectSysRoleByPrimaryKey(sysUser.getRoleId());
        Optional.ofNullable(sysRole).orElseThrow(() -> new ServiceException(MyExceptionCode.SYS_ROLE_NOT_EXIST));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> set=new HashSet<>();
        set.add(sysRole.getCode());
        info.setRoles(set);
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 登陆认证
     * 在 JWT 的 Token 中因为已将用户名和密码通过加密处理整合到一个加密串中，
     * 验证token成功就是验证密码成功
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken=(JWTToken)authcToken;
        String username=jwtToken.getUsername();
        String password=jwtToken.getPassword();
        SysUser member = sysUserService.selectByUsername(username);
        // 用户不会空
        if (member != null) {
            // 判断是否禁用
            if (member.getStat().equals(EnumSysUser.disableStatus)) {
                throw new ServiceException(MyExceptionCode.SYS_USER_STATUS_EXCEPTION);
            }
            return new SimpleAuthenticationInfo(username, password, getName());
        } else {
            throw new ServiceException(MyExceptionCode.SYS_USER_NOT_EXIST);
        }
    }

}
