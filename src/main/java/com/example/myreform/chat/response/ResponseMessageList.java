package com.example.myreform.chat.response;


import com.example.myreform.chat.domain.Message;
import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseMessageList extends ResponseType{

    private List<Message> messages;

    public ResponseMessageList(ExceptionCode exceptionCode, List<Message> messages) {
        super(exceptionCode);
        this.messages = messages;
    }

}
