package com.zhangwx.core;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 创建 JWTToken 替换 Shiro 原生 Token
 * Shiro 原生的 Token 中存在用户名和密码以及其他信息 [验证码，记住我]
 * 在 JWT 的 Token 中因为已将用户名和密码通过加密处理整合到一个加密串中，所以只需要一个 token 字段即可
 */
public class JWTToken implements AuthenticationToken {

    private String username;
    private String password;

    public JWTToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
