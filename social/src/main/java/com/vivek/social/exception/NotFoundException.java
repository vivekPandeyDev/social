package com.vivek.social.exception;


import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 473536011764154409L;

    public NotFoundException(String message) {
	super(message);
    }

}
