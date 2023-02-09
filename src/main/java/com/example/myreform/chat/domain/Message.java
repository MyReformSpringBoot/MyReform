package com.example.myreform.chat.domain;

import com.example.myreform.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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
        //this.chatRoom = chatRoom;
        /*if (messageType.equals(MessageType.TALK))
        chatRoom.getMessages().add(this);*/
    }
}
