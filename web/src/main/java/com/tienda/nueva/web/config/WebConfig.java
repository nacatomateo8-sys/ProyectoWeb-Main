package com.tienda.nueva.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Mapea la URL de inicio
        registry.addViewController("/").setViewName("index");
        
        // Corrige el mapeo de nosotros (asumiendo que renombras el archivo a nosotros.html)
        registry.addViewController("/nosotros").setViewName("nosotros");
        
        // SOLUCIÓN AL 404: Mapea la URL /ubicacion a un archivo ubicacion.html
        registry.addViewController("/ubicacion").setViewName("ubicacion");
    }
}