package com.example.project.project.interceptor;

import com.example.project.project.service.LogUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogUrlInterceptor implements HandlerInterceptor {

    @Autowired
    private LogUrlService logUrlService;

    public LogUrlInterceptor(){}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("url accessed: {}", request.getRequestURL());
        logUrlService.registerUrl(request.getRequestURL().toString());

        return true;
    }
}