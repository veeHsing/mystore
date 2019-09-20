package com.zhangwx.shiro;

import com.zhangwx.model.SysRoleResouresMap;
import com.zhangwx.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.*;
import org.apache.shiro.web.filter.authz.*;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * url拦截
 * 规则设置
 */
@Component
public class MyFilterChainDefinitions {

    private static final String JWT_FILTER_NAME = "jwt";
    private static final String ROLE_FILTER_NAME = "role";
    @Autowired
    private SysUserService sysUserService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public LinkedHashMap<String,String> build(){
        List<SysRoleResouresMap> definitionMap=sysUserService.getFilterChainDefinitionMap();

        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        map.put("/system/login","anon");//不受拦截
        map.put("/system/info","anon");
        map.put("/system/logout","anon");
        map.put("/system/allUrl","anon");
//        map.put("/system/delete/*","jwt,role[admin]");
//        map.put("/**",JWT_FILTER_NAME);
        for (SysRoleResouresMap m : definitionMap){
            if (!StringUtils.isEmpty(m.getCode())){
                //拦截m.getCode()的请求，进入jwt和role过滤器判断是否合法
                //请求将m.getRule()值赋值给RoleFilter中isAccessAllowed的mappedValue，然后和subject中的role做比较
                map.put(m.getCode(),JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"["+m.getRule()+"]");
            }
        }
        //暂时只验证已经指定的
        map.put("/**",JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"[forbid]");
        System.out.println("+++++++++++++++++++++++++++++++++"+map.toString());
        return map;

    }


    /**
     * 权限分配时候，需要调用此方法进行动态刷新
     */
    public  void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        //**************解决shiro无法动态更新问题******************
        //重写PathMatchingFilterChainResolver
        //重新生成拦截链
        //******************************************************
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
            //2、创建 FilterChainManager
            DefaultFilterChainManager manager = new DefaultFilterChainManager();
            //3、注册 Filter
            manager.addFilter("jwt",new JJWTFilter());
            manager.addFilter("role",new RoleFilter());
            //4、注册 URL-Filter 的映射关系
            LinkedHashMap<String,String> map=build();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
            filterChainResolver.setFilterChainManager(manager);
            shiroFilter.setFilterChainResolver(filterChainResolver);

            System.out.println("更新权限成功！！");
        }
    }






}
