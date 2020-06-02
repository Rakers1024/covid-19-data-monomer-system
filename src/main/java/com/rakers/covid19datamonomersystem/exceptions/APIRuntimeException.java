package com.rakers.covid19datamonomersystem.exceptions;

public class APIRuntimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public APIRuntimeException(String message) {
        super(message);
    }

    public APIRuntimeException(Exception e) {
        super(e);
    }

    public APIRuntimeException(String message, Exception e) {
        super(message + ": " + e.getMessage(), e);
    }
}