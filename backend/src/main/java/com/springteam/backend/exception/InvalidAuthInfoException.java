package com.springteam.backend.exception;

public class InvalidAuthInfoException extends RuntimeException{
    public InvalidAuthInfoException(String message) {
        super(message);
    }

    public InvalidAuthInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthInfoException(Throwable cause) {
        super(cause);
    }
}
