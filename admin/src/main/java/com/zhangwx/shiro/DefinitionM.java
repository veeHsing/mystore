package com.zhangwx.shiro;

import com.zhangwx.model.SysRoleResouresMap;
import com.zhangwx.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * url拦截
 */
public class DefinitionM {

    @Autowired
    private SysUserService sysUserService;

    public LinkedHashMap<String,String> build(){
        List<SysRoleResouresMap> definitionMap=sysUserService.getFilterChainDefinitionMap();

        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        map.put("/system/login","anon");
        map.put("/system/logout","anon");
        map.put("/**","jwt");
        for (SysRoleResouresMap m : definitionMap){
            map.put(m.getCode(),"jwt,roles["+m.getRule()+"]");
        }
        System.out.println("+++++++++++++++++++++++++++++++++"+map.toString());
        return map;

    }
}
