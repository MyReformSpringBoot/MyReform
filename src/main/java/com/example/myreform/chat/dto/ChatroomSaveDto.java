package com.example.myreform.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatroomSaveDto {
    private String ownerNickname;
    private String senderNickname;
    private Long boardId;
}
