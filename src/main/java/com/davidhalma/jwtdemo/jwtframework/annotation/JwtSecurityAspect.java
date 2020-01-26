package com.davidhalma.jwtdemo.jwtframework.annotation;

import com.davidhalma.jwtdemo.jwtframework.config.JwtProperties;
import com.davidhalma.jwtdemo.jwtframework.config.TokenType;
import com.davidhalma.jwtdemo.jwtframework.util.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Log4j2
public class JwtSecurityAspect {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";

    private final JwtTokenUtils jwtTokenUtil;
    private final HttpServletRequest httpServletRequest;
    private final JwtProperties jwtProperties;

    public JwtSecurityAspect(JwtTokenUtils jwtTokenUtil, HttpServletRequest httpServletRequest, JwtProperties jwtProperties) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.httpServletRequest = httpServletRequest;
        this.jwtProperties = jwtProperties;
    }

    @Before("@annotation(JwtSecured)")
    public void before() {
        log.info(jwtProperties);
        jwtTokenUtil.validateToken(httpServletRequest.getHeader(AUTHORIZATION_HEADER_KEY), TokenType.JWT);
    }
}
