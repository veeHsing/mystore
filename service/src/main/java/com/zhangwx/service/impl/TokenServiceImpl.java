package com.zhangwx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhangwx.model.SysUser;
import com.zhangwx.service.TokenService;
import com.zhangwx.util.JJWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    /**
     * 创建access_token
     * @param user 认证主体
     * @return
     */
    @Override
    public String createToken(SysUser user) {
        Map<String, Object> claims = new HashMap<>();

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userId",user.getId());
        jsonObject.put("userName",user.getUserName());
        jsonObject.put("roleId",user.getRoleId());
        String subject = jsonObject.toJSONString();
        try {
            return JJWTUtil.createJWT(claims,subject);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Claims getToken(String token) {
        Claims claims;
        try {
            claims= JJWTUtil.parseJWT(token);
        }catch (Exception e){
            claims=null;
        }
        return claims;
    }

    @Override
    public boolean checkToken() {
        return false;
    }

    @Override
    public boolean deleteToken() {
        return false;
    }






}
