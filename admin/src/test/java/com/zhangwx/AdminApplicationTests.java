package com.zhangwx;

import com.alibaba.fastjson.JSON;
import com.zhangwx.model.SysRoleResourcesExample;
import com.zhangwx.shiro.JJWTFilter;
import com.zhangwx.util.ResultsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class)
public class AdminApplicationTests {

//    @Autowired
//    private  RedisTemplate redisTemplate;
//
//    @Autowired
//    private JJWTFilter jjwtFilter;

    @Test
    public void contextLoads() {
//        System.out.println(jjwtFilter.getApplicationName());
//        SysRoleResourcesExample example=new SysRoleResourcesExample();
//        SysRoleResourcesExample.Criteria criteria=example.createCriteria();
//        criteria.andRoleIdEqualTo(55l);
//
//        List<SysRoleResources> list= sysRoleResourcesMapper.selectByExample(example);
        System.out.println(111);


    }

}
