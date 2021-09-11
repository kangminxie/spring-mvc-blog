package com.kangmin.app.exception;

public class WebSeverException extends RuntimeException {

    public WebSeverException() {
    }

    public WebSeverException(final String message) {
        super(message);
    }

    public WebSeverException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
