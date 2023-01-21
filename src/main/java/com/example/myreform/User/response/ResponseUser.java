package com.example.myreform.User.response;

import com.example.myreform.validation.ExceptionCode;

public class ResponseUser {
    private final int status;
    private final String code;
    private final String message;

    public ResponseUser(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus().getValue();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
