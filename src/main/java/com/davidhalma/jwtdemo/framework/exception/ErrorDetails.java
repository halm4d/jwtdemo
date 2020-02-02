package com.davidhalma.jwtdemo.framework.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ErrorDetails {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;


    public ErrorDetails(Date timestamp, HttpStatus httpStatus, String message, String path) {
        this.timestamp = timestamp;
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.path = path;
    }
}
