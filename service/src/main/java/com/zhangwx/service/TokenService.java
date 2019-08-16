package com.zhangwx.service;


import com.zhangwx.model.SysUser;
import io.jsonwebtoken.Claims;

public interface TokenService {

    public String createToken(SysUser user);
    public Claims getToken(String token);
    public boolean checkToken();
    public boolean deleteToken();


}
