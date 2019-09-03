package com.zhangwx.shiro;

import com.zhangwx.model.SysRoleResouresMap;
import com.zhangwx.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * url拦截
 * 规则设置
 */
public class DefinitionM {

    private static final String JWT_FILTER_NAME = "jwt";
    private static final String ROLE_FILTER_NAME = "role";
    @Autowired
    private SysUserService sysUserService;

    public LinkedHashMap<String,String> build(){
        List<SysRoleResouresMap> definitionMap=sysUserService.getFilterChainDefinitionMap();

        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        map.put("/system/login","anon");//不受拦截
        map.put("/system/logout","anon");
        for (SysRoleResouresMap m : definitionMap){
            if (!StringUtils.isEmpty(m.getCode())){
                //拦截m.getCode()的请求，进入jwt和role过滤器判断是否合法
                //请求将m.getRule()值赋值给RoleFilter中isAccessAllowed的mappedValue，然后和subject中的role做比较
                map.put(m.getCode(),JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"["+m.getRule()+"]");
            }
        }
        map.put("/**",JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"[forbid]");
        System.out.println("+++++++++++++++++++++++++++++++++"+map.toString());
        return map;

    }
}
