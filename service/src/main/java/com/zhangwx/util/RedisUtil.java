package com.zhangwx.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public  void set(String k,Object v){
        redisTemplate.opsForValue().set(k,v);
    }

    public Object get(String k){
        return redisTemplate.opsForValue().get(k);
    }
}
