package com.example.myreform.chat.dto;

import com.example.myreform.chat.domain.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSaveDto {
    private Message.MessageType type;
    private int chatroomId;
    private String userId;
    private String message;
}