package com.example.myreform.chat.response;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseChatroomList extends ResponseType {
    private List<ChatRoomInfo> rooms;

    public ResponseChatroomList(ExceptionCode exceptionCode, List<ChatRoomInfo> rooms) {
        super(exceptionCode);
        this.rooms = rooms;
    }
}
