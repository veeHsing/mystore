package com.zhangwx.config;

import com.zhangwx.interceptor.ShiroRuleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 定义spring全局拦截器
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurationSupport {


    @Bean
    public ShiroRuleInterceptor shiroRuleInterceptor(){
        return new ShiroRuleInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shiroRuleInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/system/login")
                .excludePathPatterns("/system/logout");
        super.addInterceptors(registry);
    }

//    @Override
//    protected void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//        super.addCorsMappings(registry);
//    }
}
