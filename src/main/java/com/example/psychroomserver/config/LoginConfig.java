package com.example.psychroomserver.config;

import com.example.psychroomserver.Interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Deprecated(since = "被删除的JWT拦截器")
public class LoginConfig implements WebMvcConfigurer {
    @Value("${jwt.secret.key}")
    private String secretKey;

    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AdminLoginInterceptor(secretKey))
//                .addPathPatterns("/api/v1/**/*")
//                .excludePathPatterns("/api/v1/**/login",
//                        "/api/v1/**/logout/**",
//                        "/api/v1/**/register/**",
//                        "/api/v1/common/captcha");
    }
}
