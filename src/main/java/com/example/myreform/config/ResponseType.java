package com.example.myreform.config;

import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseType {
    private final int status;
    private final String code;
    private final String message;

    public ResponseType(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus().getValue();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
