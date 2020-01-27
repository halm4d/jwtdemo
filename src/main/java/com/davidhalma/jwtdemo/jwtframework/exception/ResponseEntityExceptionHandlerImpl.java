package com.davidhalma.jwtdemo.jwtframework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public final ResponseEntity handleNotFoundException(JwtTokenException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED, ex.getMessage(), getRequestURI(request));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
