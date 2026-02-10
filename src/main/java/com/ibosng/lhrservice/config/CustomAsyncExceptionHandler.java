package com.ibosng.lhrservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Void async method would not show exceptions without it
 * */
@Component("lhrCustomAsyncExceptionHandler")
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Method {}, returned exception {}, for params {}", method, ex.getMessage(), params);
        log.error("Stack trace:", ex);
    }
}
