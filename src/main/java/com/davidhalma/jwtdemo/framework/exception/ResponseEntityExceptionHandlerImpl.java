package com.davidhalma.jwtdemo.framework.exception;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ResponseEntityExceptionHandlerImpl extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ErrorDetails> handleInternalException(Throwable ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), getRequestURI(request));
        log.error(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    private String getRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
