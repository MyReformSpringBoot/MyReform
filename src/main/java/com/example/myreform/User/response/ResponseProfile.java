package com.example.myreform.User.response;

import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseProfile extends ResponseUser {
    private Object data;

    public ResponseProfile(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode);
        this.data = data;
    }
}
