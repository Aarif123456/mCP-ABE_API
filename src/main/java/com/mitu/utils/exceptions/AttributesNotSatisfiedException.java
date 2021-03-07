package com.mitu.utils.exceptions;

/**
 * @author mitu
 */
public class AttributesNotSatisfiedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -4016784326622581099L;

    public AttributesNotSatisfiedException() {
    }

    public AttributesNotSatisfiedException(String message) {
        super(message);
    }

    public AttributesNotSatisfiedException(Throwable cause) {
        super(cause);
    }

    public AttributesNotSatisfiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
