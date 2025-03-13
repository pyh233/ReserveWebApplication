package com.example.psychroomserver.config;

//import cn.dev33.satoken.stp.StpLogic;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import com.example.psychroomserver.Interceptor.SecurityHandlerInterceptor;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    SecurityHandlerInterceptor securityHandlerInterceptor;

    @Autowired
    public void setSecurityHandlerInterceptor(SecurityHandlerInterceptor securityHandlerInterceptor) {
        this.securityHandlerInterceptor = securityHandlerInterceptor;
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityHandlerInterceptor)
                .addPathPatterns("/api/v1/**/*")
                .excludePathPatterns("/api/v1/**/login",
                        "/api/v1/**/logout/**",
                        "/api/v1/**/register/**",
                        "/api/v1/common/captcha");
    }
}
