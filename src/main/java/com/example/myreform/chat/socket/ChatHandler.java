package com.example.myreform.chat.socket;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.chat.domain.Message;
import com.example.myreform.chat.repository.ChatRoomRepository;
import com.example.myreform.chat.repository.MessageRepository;
import com.example.myreform.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        handleActions(session, payload, chatService);
    }

    public <TextMessage> void sendMessage(String message, ChatService chatService) {
        TextMessage text = (TextMessage) message;
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, text));
    }

    public void handleActions(WebSocketSession session, String chatMessage, ChatService chatService) {
        System.out.println("ChatHandler.handleActions");
        System.out.println("session = " + session + ", chatMessage = " + chatMessage + ", chatService = " + chatService);

        List<String> values = List.of(chatMessage.split("/"));
        Long chatroomIdValue = Long.valueOf(values.get(0));
        String nicknameValue = values.get(1);
        String messageValue = values.get(2);

        ChatRoom findChatRoom = chatRoomRepository.findByChatroomId(chatroomIdValue);
        List<Message> messages = messageRepository.findByChatroomId(chatroomIdValue);

        Message.MessageType typeValue;
        Message result, result2;
        boolean enter = false;

        for (Message message : messages) {
            if (Objects.equals(message.getNickname(), nicknameValue)) {
                enter = true;
                break;
            }
        }

        if (enter || messages.isEmpty()) {
            messageValue = nicknameValue + "님이 입장했습니다. " + messageValue;
        }

        typeValue = Message.MessageType.TALK;
        result2 = Message.builder()
                .messageType(typeValue)
                .chatRoom(findChatRoom)
                .nickname(nicknameValue)
                .message(messageValue)
                .build();

        /*if (chatMessage.getType().equals(Message.MessageType.EXIT)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getNickname() + "님이 퇴장했습니다.");
        }*/

        sessions.add(session);
        messageRepository.save(result2); // DB에 저장되는 type은 0=ENTER, 1=TALK, 2=EXIT
        sendMessage(messageValue, chatService);
    }
}