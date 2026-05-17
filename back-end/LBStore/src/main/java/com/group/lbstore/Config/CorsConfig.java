package com.group.lbstore.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho mọi API (VD: /api/products, /api/v1/banners)
                .allowedOrigins("http://localhost:5173", "http://localhost:3000") // Cấp quyền cho origin Frontend React (Vite/CRA)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Cho phép các hành động HTTP
                .allowedHeaders("*") 
                .allowCredentials(true); // Cho phép gửi kèm cookie/token
    }
}
