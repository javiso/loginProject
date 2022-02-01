package com.example.project.project.configuration;

import com.example.project.project.interceptor.LogUrlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfigurator implements WebMvcConfigurer {

    @Bean
    public LogUrlInterceptor logUrlInterceptor() {
        return new LogUrlInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(logUrlInterceptor());
    }
}