package com.media.social.gateway.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class RestClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 473536011764154409L;

    public RestClientException(String message) {
        super(message);
    }




}