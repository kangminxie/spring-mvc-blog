package com.kangmin.app.exception;

//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class WebResourceNotFoundException extends RuntimeException {

    public WebResourceNotFoundException() {
    }

    public WebResourceNotFoundException(final String message) {
        super(message);
    }

    public WebResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
