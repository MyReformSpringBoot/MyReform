package com.example.myreform.chat.response;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseChatroomList extends ResponseType {
    private List<ChatRoom> rooms;

    public ResponseChatroomList(ExceptionCode exceptionCode, List<ChatRoom> rooms) {
        super(exceptionCode);
        this.rooms = rooms;
    }

    public void addInfo(ChatRoom data) {
        this.rooms.add(data);
    }
}
