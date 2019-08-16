package com.zhangwx;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhangwx.constants.Constants;
import com.zhangwx.dao.SysRoleResourcesMapper;
import com.zhangwx.exception.ServiceException;
import com.zhangwx.input.LoginInput;
import com.zhangwx.model.SysResources;
import com.zhangwx.model.SysRoleResources;
import com.zhangwx.model.SysRoleResourcesExample;
import com.zhangwx.model.SysUser;
import com.zhangwx.output.SysResourcesTree;
import com.zhangwx.output.SysUserInfoOutput;
import com.zhangwx.service.SysUserService;
import com.zhangwx.service.TokenService;
import com.zhangwx.util.DatesUtil;
import com.zhangwx.util.MD5Util;
import com.zhangwx.util.UserRequest;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class ServiceApplicationTests {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private SysRoleResourcesMapper sysRoleResourcesMapper;

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void contextLoads() {
//        PageInfo<SysUser> pageInfo=sysUserService.getSysUserList(SysUserListInputsysUserListInput);
//        System.out.println(111);
        List a=sysUserService.getSysResourcesList();
//        SysResources parent= new SysResources();
//        SysResourcesTree son =new SysResourcesTree();
//        parent.setId(11);
//        BeanUtils.copyProperties(parent,son);
        System.out.println(111);

    }

}
