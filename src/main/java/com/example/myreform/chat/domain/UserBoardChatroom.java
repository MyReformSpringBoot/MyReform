package com.example.myreform.chat.domain;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.User.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_BOARD_CHATROOM")
public class UserBoardChatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBoardChatroomId;

    private Long chatroomId;
    private LocalDateTime lastTime;

    private String ownerNickname;
    private String senderNickname;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public UserBoardChatroom(Board board, String owner, String sender){
        this.board = board;
        this.ownerNickname = owner;
        this.senderNickname = sender;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setLastTime(LocalDateTime time) {
        this.lastTime = time;
    }
}
