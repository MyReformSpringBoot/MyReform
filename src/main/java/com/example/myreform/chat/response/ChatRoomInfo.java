package com.example.myreform.chat.response;

import com.example.myreform.Board.domain.BoardImage;
import com.example.myreform.Image.domain.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomInfo {

    private Long chatroomId;
    private String ownerUserId;
    private String senderUserId;
    private String ownerNickname;
    private String senderNickname;

    private String boardTitle;
    private Long boardId;

    private String lastMessage;
    private String time;

    private Integer price;
    private List<BoardImage> imageList;

    @Builder
    public ChatRoomInfo(Long chatroomId, String ownerUserId, String senderUserId,
                        String ownerNickname, String senderNickname, Long boardId,
                        Integer price, List<BoardImage> imageList, String boardTitle, String time) {
        this.chatroomId = chatroomId;
        this.boardId = boardId;
        this.ownerUserId = ownerUserId;
        this.senderUserId = senderUserId;
        this.ownerNickname = ownerNickname;
        this.senderNickname = senderNickname;
        this.boardTitle = boardTitle;
        this.time = time;
        this.price = price;
        this.imageList = imageList;
    }
}

