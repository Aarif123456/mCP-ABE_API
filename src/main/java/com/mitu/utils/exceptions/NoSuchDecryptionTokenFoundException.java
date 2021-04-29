package com.mitu.utils.exceptions;

/**
 * @author mitu
 */
public class NoSuchDecryptionTokenFoundException extends Exception {
    private static final long serialVersionUID = -4413958008944678131L;

    public NoSuchDecryptionTokenFoundException() {
    }

    public NoSuchDecryptionTokenFoundException(String message) {
        super(message);
    }

    public NoSuchDecryptionTokenFoundException(Throwable cause) {
        super(cause);
    }

    public NoSuchDecryptionTokenFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
