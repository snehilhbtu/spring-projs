package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogApiException extends RuntimeException{

    private HttpStatus status;

    public BlogApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    private  String message;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
