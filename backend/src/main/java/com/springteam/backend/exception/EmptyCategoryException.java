package com.springteam.backend.exception;

public class EmptyCategoryException extends RuntimeException{
    public EmptyCategoryException(String message) {
        super(message);
    }

    public EmptyCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCategoryException(Throwable cause) {
        super(cause);
    }
}
