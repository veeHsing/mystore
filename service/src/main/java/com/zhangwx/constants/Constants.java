package com.zhangwx.constants;

import com.zhangwx.util.JJWTUtil;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.UUID;

/**
 * 全局常量类
 */
public class Constants {

    public static final String APP_NAME="自己想着搭框架";
    public static final Integer TOKEN_EXP=1*24*60*60*1000;
    //jwt秘钥
    public static final String JWT_SECRET="IqrnwdGr2zbwBXMRGk6V6INqxajlm9i3gM0wkYUsPa4aIE9gsXbd82bkEbsUOmnS";
    // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
    public static final String JWT_ID = UUID.randomUUID().toString();
    // issuer：jwt签发人
    public static final String JWT_ISSUER = "zhangwx";
    //设置过期时间
    public static final long JWT_TTLMILLIS = 86400000;
    // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
    public static final SignatureAlgorithm JWT_SIGNATUREALGORITHM =SignatureAlgorithm.HS256;

    public static  final  String JWT_BODY="jwtBody";
}
