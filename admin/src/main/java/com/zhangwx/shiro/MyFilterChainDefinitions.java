package com.zhangwx.shiro;

import com.zhangwx.model.SysRoleResouresMap;
import com.zhangwx.service.SysUserService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        map.put("/system/logout","anon");
        for (SysRoleResouresMap m : definitionMap){
            if (!StringUtils.isEmpty(m.getCode())){
                //拦截m.getCode()的请求，进入jwt和role过滤器判断是否合法
                //请求将m.getRule()值赋值给RoleFilter中isAccessAllowed的mappedValue，然后和subject中的role做比较
                map.put(m.getCode(),JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"["+m.getRule()+"]");
            }
        }
        //暂时只验证已经指定的
//        map.put("/**",JWT_FILTER_NAME+","+ROLE_FILTER_NAME+"[forbid]");
        map.put("/**",JWT_FILTER_NAME);
        System.out.println("+++++++++++++++++++++++++++++++++"+map.toString());
        return map;

    }


    /**
     * 权限分配时候，需要调用此方法进行动态刷新
     * @param shiroFilterFactoryBean
     */
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(build());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }
    }



}
