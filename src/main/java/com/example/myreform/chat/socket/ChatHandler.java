package com.example.myreform.chat.socket;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.chat.domain.Message;
import com.example.myreform.chat.dto.MessageSaveDto;
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

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    private Set<WebSocketSession> sessions = new HashSet<>();

    public <T> void sendMessage(Message message, ChatService chatService) {
        //messageRepository.save(message);
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);
        MessageSaveDto messageSaveDto = objectMapper.readValue(payload, MessageSaveDto.class);
        ChatRoom chatRoom = chatRoomRepository.findByChatroomId(Long.parseLong(String.valueOf(
                messageSaveDto.getChatroomId())));
        Message chatMessage = Message.builder()
                .messageType(messageSaveDto.getType())
                .chatRoom(chatRoom)
                .nickname(messageSaveDto.getNickname())
                .message(messageSaveDto.getMessage())
                .build();
        handleActions(session, chatMessage, chatService);
    }

    public void handleActions(WebSocketSession session, Message chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(Message.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getNickname() + "님이 입장했습니다.");
        }

        if (chatMessage.getType().equals(Message.MessageType.EXIT)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getNickname() + "님이 퇴장했습니다.");
        }

        sendMessage(chatMessage, chatService);
    }
}