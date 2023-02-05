package com.example.myreform.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSaveDto {
    private String chatroomId;
    private Long senderId;
    private String contents;
}