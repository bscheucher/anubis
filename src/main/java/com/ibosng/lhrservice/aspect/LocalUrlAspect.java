package com.ibosng.lhrservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Replaces hosts in url to localhost for local testing
 * </br>
 * To use aspect add to VM options: <b>-Dspring.profiles.active=local</b>
 * </br>
 * Or use profile <b>local</b>
 * */
@Aspect
@Component
@Slf4j
@EnableAspectJAutoProxy
@Profile("local")
public class LocalUrlAspect {

    private static final String[] TARGET_HOSTS = {"validation-service.backend", "lhr-service.backend", "ai-service.ai-engine", "moxis-service.backend", "natif-service.backend"};
    private static final String REPLACEMENT_HOST = "localhost";

    @Around("execution(* com.ibosng.lhrservice.services.RestService.sendRequest(..)) || " +
            "execution(* com.ibosng.lhrservice.services.RestService.sendRequestParametrized(..))")
    public Object rewriteEndpointInRestService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length > 1 && args[1] instanceof String originalUrl) {
            for (String targetHost : TARGET_HOSTS) {
                if (originalUrl.contains(targetHost)) {
                    String modifiedUrl = originalUrl.replace(targetHost, REPLACEMENT_HOST);
                    log.debug("Rewriting endpoint URL from '{}' to '{}'", originalUrl, modifiedUrl);
                    args[1] = modifiedUrl;
                }
            }
        }

        return joinPoint.proceed(args);
    }
}