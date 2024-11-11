package com.hhplus.commerce.interfaces.intetceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.commerce.interfaces.filter.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        log.info(
                "\nRequest URL: {} " +
                "\nHTTP Method: {} " +
                "\nRequest Parameters: {} " +
                "\nRequest Body: {}",
                request.getRequestURL(),
                request.getMethod(),
                objectMapper.writeValueAsString(request.getParameterMap()),
                objectMapper.readTree(cachedRequest.getInputStream())
        );

        return true;
    }
}
