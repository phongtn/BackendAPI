package com.wind.config.log;

import org.aspectj.lang.annotation.Pointcut;

/**
 * The PointCuts create dummy methods required to hold @Pointcut annotations.
 * This prevents a repetition of code
 *
 * @author PhongTn
 */
public class LoggingPointcut {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void service() {
    }

    @Pointcut("within(@org.springframework.stereotype.Component *)")
    public void component() {
    }
}