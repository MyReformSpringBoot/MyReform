package com.example.myreform.chat.domain;

import com.example.myreform.config.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CHATROOM")
@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜, 시간
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

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