package com.springboot.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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

    // debe llamarse asi. Metodo Para registrar un controlador de vista. que serian controladores parametrizables o estaticos que cargan la vista sin logica de controlador
    public void addViewControllers(ViewControllerRegistry registry){

        registry.addViewController("/error_403").setViewName("error_403"); // ruta url, getMapping
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
