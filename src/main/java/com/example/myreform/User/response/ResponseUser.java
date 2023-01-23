package com.example.myreform.User.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseUser extends ResponseType {
    public ResponseUser(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
