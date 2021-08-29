package com.kangmin.app.exception;

public class WebUserUnauthorizedException extends RuntimeException {

    public WebUserUnauthorizedException() {
    }

    public WebUserUnauthorizedException(final String message) {
        super(message);
    }

    public WebUserUnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
