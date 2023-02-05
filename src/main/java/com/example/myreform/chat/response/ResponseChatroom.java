package com.example.myreform.chat.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseChatroom extends ResponseType {
    private Object data;

    public ResponseChatroom(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode);
        this.data = data;
    }
}
