package com.example.myreform.chat.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.chat.domain.Message;
import com.example.myreform.chat.dto.ChatroomFindDto;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import com.example.myreform.chat.dto.MessageFindDto;
import com.example.myreform.chat.repository.ChatRoomRepository;
import com.example.myreform.chat.repository.MessageRepository;
import com.example.myreform.chat.response.*;
import com.example.myreform.config.Time;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ChatService implements ChatServiceImpl {

    private final ObjectMapper objectMapper;

    public <TextMessage> void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(new org.springframework.web.socket.TextMessage(message.toString()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private final ChatRoomRepository chatRoomRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public Object findByNickname(ChatroomFindDto chatroomFindDto) { // 채팅방 조회
        String userId = chatroomFindDto.getUserId();
        Optional<User> user = userRepository.findById(userId);
        List<ChatRoomInfo> result = null;
        if (user.isPresent()) {
            List<ChatRoom> rooms = chatRoomRepository.findByOwnerUserIdOrSenderUserId(userId, userId);

            Collections.reverse(rooms);
            if (rooms.isEmpty()) {
                return new ResponseChatroomEmpty(ExceptionCode.CHATROOM_LIST_NOT_FOUND);
            }
            result = new ArrayList<>();

            for (ChatRoom chatRoom : rooms) {
                List<Message> messages = messageRepository.findByChatroomId(chatRoom.getChatroomId());
                Optional<User> owner = userRepository.findById(chatRoom.getOwnerUserId());
                Optional<User> sender = userRepository.findById(chatRoom.getSenderUserId());
                if (owner.isPresent() && sender.isPresent()) {
                    Board board = boardRepository.findBoardByBoardIdAndStatusEquals(chatRoom.getBoardId(), 1);
                    ChatRoomInfo chatRoomInfo = ChatRoomInfo.builder()
                            .boardTitle(chatRoom.getBoardTitle())
                            .ownerUserId(chatRoom.getOwnerUserId())
                            .senderUserId(chatRoom.getSenderUserId())
                            .ownerNickname(owner.get().getNickname())
                            .senderNickname(sender.get().getNickname())
                            .chatroomId(chatRoom.getChatroomId())
                            .boardId(chatRoom.getBoardId())
                            .time(Time.calculateTime(chatRoom.getUpdateAt()))
                            .price(board.getPrice())
                            .imageList(board.getBoardImages())
                            .build();
                    chatRoomInfo.setLastMessage(checkLastMessage(messages));
                    result.add(chatRoomInfo);
                }
            }
        }
        return new ResponseChatroomList(ExceptionCode.CHATROOM_LIST_GET_OK, result);
    }

    private String checkLastMessage(List<Message> messages) {
        if (messages.size() > 0) {
            return messages.get(messages.size() - 1).getMessage();
        } else {
            return null;
        }
    }

    public Object findMessages(MessageFindDto messageFindDto) { // 메세지 조회
        Long chatroomId = messageFindDto.getChatroomId();
        List<Message> messages = messageRepository.findByChatroomId(chatroomId);
        if (messages.isEmpty()) {
            return new ResponseChatroomEmpty(ExceptionCode.MESSAGE_LIST_NOT_FOUND);
        }
        return new ResponseMessageList(ExceptionCode.MESSAGE_LIST_GET_OK, messages);
    }

    @Override
    public Object findChatroomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatroomId(roomId);
        if(chatRoom == null) {
            return new ResponseChatroomEmpty(ExceptionCode.CHATROOM_NOT_FOUND);
        }
        return new ResponseChatroom(ExceptionCode.CHATROOM_GET_OK, chatRoom);
    }

    @Override // 채팅방 생성
    public Object save(ChatroomSaveDto chatroomSaveDto) {
        Board board = boardRepository.findBoardByBoardIdAndStatusEquals(Long.parseLong(String.valueOf(chatroomSaveDto.getBoardId())), 1);
        Optional<User> sender = userRepository.findById(chatroomSaveDto.getSenderId());
        Optional<User> owner = userRepository.findByNickname(board.getUser().getNickname());

        if (validation(board, owner, sender)) {
            ChatRoom room = ChatRoom.builder()
                    .boardId(board.getBoardId())
                    .ownerUserId(owner.get().getId())
                    .senderUserId(sender.get().getId())
                    .boardTitle(board.getTitle())
                    .build();

            ChatRoomInfo chatRoomInfo = ChatRoomInfo.builder()
                    .senderNickname(sender.get().getNickname())
                    .ownerNickname(owner.get().getNickname())
                    .boardId(board.getBoardId())
                    .ownerUserId(owner.get().getId())
                    .senderUserId(sender.get().getId())
                    .boardTitle(board.getTitle())
                    .build();

            Optional<ChatRoom> chatRooms = chatRoomRepository.findByBoardIdAndOwnerUserIdAndSenderUserId(
                    board.getBoardId(), owner.get().getId(), sender.get().getId());
            if (chatRooms.isEmpty()) {
                chatRoomRepository.save(room);

                return new ResponseChatroom(ExceptionCode.CHATROOM_CREATE_OK, room);
            } else {
                chatRoomInfo.setLastMessage(checkLastMessage(chatRooms.get().getMessages()));
                return new ResponseChatroom(ExceptionCode.CHATROOM_CREATE_ERROR, chatRooms.get());
            }
        }
        else {
            return new ResponseChatroomEmpty(ExceptionCode.CHATROOM_UPDATE_INVALID);
        }
    }

    private boolean validation(Board board, Optional<User> owner, Optional<User> sender) {
        //System.out.println("중복");
        if (board == null || owner.isEmpty() || sender.isEmpty()) {
            //System.out.println("빈 정보");
            return false;
        }
        else return !owner.get().getUserId().equals(sender.get().getUserId());
    }
}
