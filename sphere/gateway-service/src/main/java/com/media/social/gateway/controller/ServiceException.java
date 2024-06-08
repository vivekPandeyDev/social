package com.media.social.gateway.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 473536011764154409L;

    private  HttpStatus status = HttpStatus.BAD_REQUEST;
    private  String title = "SERVICE EXCEPTION";
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ServiceException(String message, String title) {
        super(message);
        this.title = title;
    }

    public ServiceException(String message, HttpStatus status, String title) {
        this(message,status);
        this.title = title;
    }

}