package com.group.lbstore.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để truy cập folder "uploads" qua URL /uploads/**
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

        // Dùng toUri() để tạo đúng "file:///app/uploads/" trên Linux (Docker)
        // Tránh lỗi ghép tay: "file:/" + "/app/uploads" = "file://app/uploads/" (sai!)
        String uploadLocation = uploadDir.toUri().toString();
        if (!uploadLocation.endsWith("/")) {
            uploadLocation += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
    }
}
