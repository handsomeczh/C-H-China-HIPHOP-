package com.czh.chbackend.config;

import com.czh.chbackend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
//  todo 记得开启拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/user/login/**","/register","loginByCode","/getCode/**","/music/**"); // Exclude specific paths if neede

    }

    /**
     * 解决前端跨域问题
     * @param registry
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry){
//        registry.addMapping("/api/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET","POST","PUT","DELETE")
//                .allowedHeaders("*");
//    }
}
