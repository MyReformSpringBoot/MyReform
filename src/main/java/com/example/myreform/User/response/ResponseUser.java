package com.example.myreform.User.response;

import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private final int status;
    private final String code;
    private final String message;
    @Nullable
    @Autowired
    private String token;
    public ResponseUser(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus().getValue();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public ResponseUser(ExceptionCode exceptionCode, @Nullable String token) {
        this.status = exceptionCode.getStatus().getValue();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
        this.token = token;
    }
}
