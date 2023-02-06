package com.example.myreform.Like.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;

public class ResponseLike extends ResponseType {
    private Object data;

    public ResponseLike(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode);
        this.data = data;
    }
}
