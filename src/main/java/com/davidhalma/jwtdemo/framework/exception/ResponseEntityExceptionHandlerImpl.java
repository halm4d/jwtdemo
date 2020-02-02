package com.davidhalma.jwtdemo.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Date;

@RestController
@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundException(Throwable ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED, ex.getMessage(), getRequestURI(request));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
