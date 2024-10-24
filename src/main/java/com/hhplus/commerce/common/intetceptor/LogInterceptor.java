package com.hhplus.commerce.common.intetceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("IP Address: {}", request.getRemoteAddr());
        log.info("Request Parameters: {}", request.getParameterMap().toString());

        return true;
    }
}
