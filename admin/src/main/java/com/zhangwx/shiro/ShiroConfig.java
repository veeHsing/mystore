package com.zhangwx.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static final String JWT_FILTER_NAME = "jwt";
    private static final String ROLE_FILTER_NAME = "role";

    //自定义realm，实现登录授权流程
    @Bean
    public MyShiroRealm shiroDatabaseRealm(){
        return  new MyShiroRealm();
    }

    @Bean
    public DefinitionM definitionM(){
        return new DefinitionM();
    }


    //配置shiro过滤器
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        //1.定义shiroFactoryBean
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //2.设置securityManager
        factoryBean.setSecurityManager(securityManager);
        //3定义拦截器,主要获取，验证jwt，并把jwt中的用户信息存入request中，方便调用
        Map<String, Filter> filterMap = new HashMap<>();
        //首先做认证拦截，判断jwt token是否合法
        filterMap.put(JWT_FILTER_NAME, new JJWTFilter());
        //让后做授权拦截，判断当前请求是否合法权限
        filterMap.put(ROLE_FILTER_NAME, new RoleFilter());
        factoryBean.setFilters(filterMap);
        //anon:所有url都可以匿名访问
        //jwt，role[admin]:首先调用jwt拦截器，通过后调用role拦截器
        factoryBean.setFilterChainDefinitionMap(definitionM().build());
        return factoryBean;
    }

    /**
     * 配置securityManager 管理subject（默认）,并把自定义realm交由manager
     * @param shiroDatabaseRealm
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(MyShiroRealm shiroDatabaseRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroDatabaseRealm);
        //非web关闭sessionManager(官网有介绍)
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator storageEvaluator = new DefaultSessionStorageEvaluator();
        storageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(storageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        return securityManager;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
