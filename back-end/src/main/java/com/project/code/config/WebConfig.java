package com.project.code.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS for all endpoints
        registry.addMapping("/**")
                .allowedOrigins("*")  // Add your frontend URL here
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Specify allowed methods
                .allowedHeaders("*");  // You can restrict headers if needed
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path frontEndPath = Paths.get("front-end").toAbsolutePath().normalize();
        String frontEndLocation = frontEndPath.toUri().toString();

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", frontEndLocation)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
