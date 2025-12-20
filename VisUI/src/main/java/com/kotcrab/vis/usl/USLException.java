package com.kotcrab.vis.usl;

/**
 * Exception that is thrown when USL file parsing fails
 */
public class USLException extends RuntimeException {
    public USLException(String message) {
        super(message);
    }

    public USLException(Throwable cause) {
        super(cause);
    }

    public USLException(String message, Throwable cause) {
        super(message, cause);
    }
}
