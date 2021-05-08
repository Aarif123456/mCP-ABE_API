package com.mitu.utils.exceptions;

public class MalformedAttributesException extends Exception {
    public MalformedAttributesException() {
    }

    public MalformedAttributesException(String message) {
        super(message);
    }

    public MalformedAttributesException(Throwable cause) {
        super(cause);
    }

    public MalformedAttributesException(String message, Throwable cause) {
        super(message, cause);
    }
}