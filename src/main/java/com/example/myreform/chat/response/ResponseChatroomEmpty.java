package com.example.myreform.chat.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;

public class ResponseChatroomEmpty extends ResponseType {

    public ResponseChatroomEmpty(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
