package com.example.myreform.chat.domain;

import com.example.myreform.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜, 시간
public class Message extends BaseEntity {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long chatroomId;

    public void setMessage(String s) {
        this.message = s;
    }

/*    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;*/

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK, EXIT
    }

    private MessageType type; // 메시지 타입
    private String nickname; // 메시지 보낸사람
    private String message; // 메시지

    @Builder
    public Message(MessageType messageType, String nickname, String message, ChatRoom chatRoom) {
        this.type = messageType;
        this.nickname = nickname;
        this.message = message;
        this.chatroomId = chatRoom.getChatroomId();

        setCreateAt(LocalDateTime.now());
        setUpdateAt(LocalDateTime.now());
    }
}
