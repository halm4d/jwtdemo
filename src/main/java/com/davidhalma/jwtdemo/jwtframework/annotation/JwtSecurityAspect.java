package com.davidhalma.jwtdemo.jwtframework.annotation;

import com.davidhalma.jwtdemo.jwtframework.service.JwtTokenValidator;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Log4j2
public class JwtSecurityAspect {

    private final JwtTokenValidator jwtTokenValidator;
    private final HttpServletRequest httpServletRequest;

    public JwtSecurityAspect(JwtTokenValidator jwtTokenValidator, HttpServletRequest httpServletRequest) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.httpServletRequest = httpServletRequest;
    }

    @Before("@annotation(JwtSecured)")
    public void before() {
        jwtTokenValidator.validateToken(httpServletRequest.getHeader("Authorization"));
    }
}
