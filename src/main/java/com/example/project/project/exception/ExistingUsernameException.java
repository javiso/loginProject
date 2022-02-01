package com.example.project.project.exception;

public class ExistingUsernameException extends RuntimeException {
    public ExistingUsernameException(String msg) {
        super(msg);
    }

    public ExistingUsernameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}