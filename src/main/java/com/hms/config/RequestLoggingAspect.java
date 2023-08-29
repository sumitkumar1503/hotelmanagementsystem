package com.hms.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class RequestLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    @Pointcut("execution(public * com.hms.*.*.*(..))")
    public void logRequest() {}

    @Before("logRequest()")
    public void logRequestDetails(JoinPoint joinPoint) {

        String requestBody  = Arrays.toString(joinPoint.getArgs());

        logger.info("Signature:  {} Request Body: {}", joinPoint.getSignature(), requestBody);
    }
}
