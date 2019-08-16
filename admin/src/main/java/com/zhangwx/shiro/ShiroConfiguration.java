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
public class ShiroConfiguration {

    private static final String JWT_FILTER_NAME = "jwt";

    /**
     * 自定义realm，实现登录授权流程
     * @return
     */
    @Bean
    public ShiroRealm shiroDatabaseRealm(){
        return  new ShiroRealm();
    }

    @Bean
    public DefinitionM definitionM(){
        return new DefinitionM();
    }



    /**
     * 配置securityManager 管理subject（默认）,并把自定义realm交由manager
     * @param shiroDatabaseRealm
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroDatabaseRealm) {
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

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setFilters(filterMap());
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(definitionM().build());
//        factoryBean.setFilterChainDefinitionMap(definitionMap());
        return factoryBean;
    }

    /**
     * 自定义拦截器，处理所有请求
     */
    private Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put(JWT_FILTER_NAME, new JJWTFilter());
        return filterMap;
    }

    /**
     * url拦截规则
     */
    private Map<String, String> definitionMap() {
        Map<String, String> definitionMap = new HashMap<>();
        definitionMap.put("/login", "anon");
        definitionMap.put("/**", JWT_FILTER_NAME);
        return definitionMap;
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
