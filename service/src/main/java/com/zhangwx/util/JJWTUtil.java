package com.zhangwx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhangwx.constants.Constants;
import com.zhangwx.constants.MyExceptionCode;
import com.zhangwx.exception.ServiceException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.rmi.server.ServerCloneException;
import java.util.Date;
import java.util.Map;

public class JJWTUtil {

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private  static  SecretKey generalKey() {
        String stringKey = Constants.JWT_SECRET;
        byte[] encodedKey = stringKey.getBytes();

        // 根据给定的字节数组使用加密算法构造一个密钥
        SecretKey key= Keys.hmacShaKeyFor(encodedKey);
        return key;
    }

    /**
     * 创建jwt
     * @param claims   私有声明
     * @param subject  JWT的主体
     * @return
     * @throws Exception
     */
    public static String createJWT(Map claims, String subject) throws Exception {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。


        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();
        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(Constants.JWT_ID)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer(Constants.JWT_ISSUER)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(key,Constants.JWT_SIGNATUREALGORITHM); // 设置签名使用的签名算法和签名使用的秘钥

        long ttlMillis=Constants.JWT_TTLMILLIS;
        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }

    public static JWTBody getBody(String jwt){
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        // 设置需要解析的jwt
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt)
                .getBody();
        JSONObject jsonObject= (JSONObject)JSON.parse(claims.getSubject());
        JWTBody jwtBody=new JJWTUtil.JWTBody(jsonObject.getLong("userId"),jsonObject.getString("userName"),jsonObject.getInteger("roleId"));
        return  jwtBody;
    }

    public static String getUserName(String jwt){
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        JSONObject jsonObject= (JSONObject)JSON.parse(claims.getSubject());
        return jsonObject.get("userName").toString();

    }

    public static Long getUserId(String jwt){
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        JSONObject jsonObject= (JSONObject)JSON.parse(claims.getSubject());
        return (Long) jsonObject.get("userId");

    }


    public static class JWTBody{
        private long userId;
        private String userName;
        private int roleId;

        public JWTBody(long userId, String userName, int roleId) {
            this.userId = userId;
            this.userName = userName;
            this.roleId = roleId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }
    }

}
