package com.springteam.backend.exception;

public class UserExistingException extends RuntimeException{

    public UserExistingException(String message) {
        super(message);
    }

    public UserExistingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistingException(Throwable cause) {
        super(cause);
    }
}
