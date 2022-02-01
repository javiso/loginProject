package com.example.project.project.exception;

public class TokenFoundException extends RuntimeException {

    public TokenFoundException(String msg) {
        super(msg);
    }

    public TokenFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}