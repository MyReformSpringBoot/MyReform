package com.example.myreform.chat.domain;

import com.example.myreform.config.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "chatroom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chatroom extends BaseEntity {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    private Long ownerId;
    private Long senderId;
    private Long boardId;

    @OneToMany(mappedBy = "chatroom")
    List<Message> messages = new ArrayList<>();


    @Builder
    public Chatroom(Long boardId, Long ownerId, Long senderId) {
        this.ownerId = ownerId;
        this.senderId = senderId;
        this.boardId = boardId;
    }

/*    public void handlerActions(WebSocketSession session, Message message, ChatService chatService) {
        if (message.getType().equals(Message.MessageType.ENTER)) {
            sessions.add(session);
            message.setContents(message.getSenderId() + "님이 입장했습니다.");
        }
        sendMessage(message, chatService);

    }

    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }*/


}
