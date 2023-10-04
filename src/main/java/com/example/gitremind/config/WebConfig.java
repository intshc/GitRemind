package com.example.gitremind.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${front.url}")
    private String frontUrl;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                //서로 다른 도메인 간의 요청 및 응답에서 쿠키 및 인증 정보가 자동으로 전송 X
                .allowCredentials(false)
                //1시간 유지
                .maxAge(3600);
    }
}