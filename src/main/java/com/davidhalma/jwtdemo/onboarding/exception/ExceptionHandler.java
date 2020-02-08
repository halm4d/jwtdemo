package com.davidhalma.jwtdemo.onboarding.exception;

import com.davidhalma.jwtdemo.framework.exception.ErrorDetails;
import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Date;

@RestController
@ControllerAdvice
@Log4j2
public class ExceptionHandler extends ExceptionHandlerExceptionResolver {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(Throwable ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.CONFLICT, ex.getMessage(), getRequestURI(request));
        log.error(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<ErrorDetails> handleAuthenticationException(Throwable ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED, ex.getMessage(), getRequestURI(request));
        log.error(ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
