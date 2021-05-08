package com.mitu.utils.exceptions;

public class MalformedPolicyException extends Exception {
    public MalformedPolicyException() {
    }

    public MalformedPolicyException(String message) {
        super(message);
    }

    public MalformedPolicyException(Throwable cause) {
        super(cause);
    }

    public MalformedPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}