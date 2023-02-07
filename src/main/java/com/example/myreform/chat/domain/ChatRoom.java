package com.example.myreform.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CHATROOM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ChatRoom {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    private String ownerNickname;
    private String senderNickname;
    private String boardTitle;

    private Long ownerId;
    private Long senderId;
    private Long boardId;

/*    @OneToMany(mappedBy = "message")
    List<Message> messages = new ArrayList<>();*/

    @Builder
    public ChatRoom(Long ownerId, Long senderId, Long boardId,
                    String ownerNickname, String senderNickname, String boardTitle) {
        this.ownerId = ownerId;
        this.senderId = senderId;
        this.boardId = boardId;
        this.ownerNickname = ownerNickname;
        this.senderNickname = senderNickname;
        this.boardTitle = boardTitle;
    }
}