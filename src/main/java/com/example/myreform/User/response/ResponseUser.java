package com.example.myreform.User.response;

import com.example.myreform.User.validation.ExceptionCode;
import org.springframework.http.HttpStatus;

public class ResponseUser {
    private final HttpStatus status;
    private final String code;
    private final String message;

    public ResponseUser(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
