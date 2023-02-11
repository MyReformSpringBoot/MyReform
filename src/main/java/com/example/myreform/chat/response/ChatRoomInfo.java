package com.example.myreform.chat.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomInfo {

    private Long chatroomId;
    private String ownerNickname;
    private String senderNickname;
    private String boardTitle;
    private Long boardId;

    private String lastMessage;
    private String time;

    @Builder
    public ChatRoomInfo(Long chatroomId, String ownerNickname, String senderNickname, Long boardId, String boardTitle, String time) {
        this.chatroomId = chatroomId;
        this.boardId = boardId;
        this.ownerNickname = ownerNickname;
        this.senderNickname = senderNickname;
        this.boardTitle = boardTitle;
        this.time = time;
    }
}
