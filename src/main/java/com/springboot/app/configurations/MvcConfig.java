package com.springboot.app.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        WebMvcConfigurer.super.addResourceHandlers(registry);

        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString(); // toUri para que lo incluya al esquema, toma el path y le agrega el esquema file.

        registry.addResourceHandler("/uploads/**")  // para registrar nuestra nueva ruta como recurso estatico
        .addResourceLocations(resourcePath);
    }
    */
}
