package com.example.myreform.chat.domain;

import com.example.myreform.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "message")
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Message extends BaseEntity {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    private Long senderId;
    private String contents;

    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type;

    @Builder
    public Message(MessageType messageType, Long senderId, String contents) {
        this.type = messageType;
        this.senderId = senderId;
        this.contents = contents;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
