package com.davidhalma.jwtdemo.jwtframework.annotation;

import com.davidhalma.jwtdemo.jwtframework.config.JwtProperties;
import com.davidhalma.jwtdemo.jwtframework.exception.BadAuthorizationHeaderException;
import com.davidhalma.jwtdemo.jwtframework.util.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Log4j2
public class JwtSecurityAspect {

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
        final String requestTokenHeader = getAuthorizationHeader();
        validateRequestTokenHeader(requestTokenHeader);
        final String jwtToken = getJwtToken(requestTokenHeader);
        jwtTokenUtil.validateToken(jwtToken);
    }

    private void validateRequestTokenHeader(String requestTokenHeader) {
        if (StringUtils.isEmpty(requestTokenHeader)){
            throw new BadAuthorizationHeaderException(jwtProperties.getHeaderKey() + " header is missing.");
        }
        if (!requestTokenHeader.startsWith(jwtProperties.getTokenPrefix() + " ")){
            throw new BadAuthorizationHeaderException(jwtProperties.getHeaderKey() + " does not begin " + jwtProperties.getTokenPrefix() + " prefix.");
        }
    }

    private String getAuthorizationHeader() {
        return httpServletRequest.getHeader(jwtProperties.getHeaderKey());
    }

    private String getJwtToken(String requestTokenHeader) {
        return requestTokenHeader.replace(jwtProperties.getTokenPrefix() + " ", "");
    }
}
