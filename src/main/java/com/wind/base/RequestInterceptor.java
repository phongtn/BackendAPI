package com.wind.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


@Slf4j
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private final String ATTRIBUTE_START_TIME = "X-Start-Time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(ATTRIBUTE_START_TIME, System.currentTimeMillis());
//        this.extractRequestHeader(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute(ATTRIBUTE_START_TIME);
        long duration = System.currentTimeMillis() - startTime;
        response.setHeader("X-Duration-Time", String.valueOf(duration));
        log.info("[{}] taken {}ms. Status: {}", request.getRequestURI(), duration, response.getStatus());
    }

    private void extractRequestHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("Header {} {}", headerName, headerValue);
        }
    }
}
