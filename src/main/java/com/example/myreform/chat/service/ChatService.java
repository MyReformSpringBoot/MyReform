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
import com.example.myreform.chat.response.ResponseChatroom;
import com.example.myreform.chat.response.ResponseChatroomEmpty;
import com.example.myreform.chat.response.ResponseChatroomList;
import com.example.myreform.chat.response.ResponseMessageList;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ChatService implements ChatServiceImpl {

    private final ObjectMapper objectMapper;

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
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
        String nickname = chatroomFindDto.getNickname();

        List<ChatRoom> rooms = chatRoomRepository.findByOwnerNicknameOrSenderNickname(nickname, nickname);
        Collections.reverse(rooms);
        if (rooms.isEmpty()) {
            return new ResponseChatroomEmpty(ExceptionCode.CHATROOM_LIST_NOT_FOUND);
        }
        List<ChatRoom> result  = new ArrayList<>();
        for (ChatRoom chatRoom : rooms) {
            List<Message> messages = messageRepository.findByChatroomId(chatRoom.getChatroomId()); // fix
            ChatRoom room = chatRoom;
            if (messages.size() > 0) {
                room.setLastMessage(messages.get(messages.size() - 1).getMessage());
            } else {
                room.setLastMessage(null);
            }
            result.add(room);
        }
        return new ResponseChatroomList(ExceptionCode.CHATROOM_LIST_GET_OK, result);
    }

    public Object findmessages(MessageFindDto messageFindDto) { // 메세지 조회
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
        Optional<User> sender = userRepository.findByNickname(chatroomSaveDto.getSenderNickname());
        Optional<User> owner = userRepository.findByNickname(board.getUser().getNickname());

        if (validation(board, owner, sender)) {
            ChatRoom room = ChatRoom.builder()
                    .boardId(board.getBoardId())
                    .ownerId(owner.get().getUserId())
                    .senderId(sender.get().getUserId())
                    .boardTitle(board.getTitle())
                    .ownerNickname(owner.get().getNickname())
                    .senderNickname(sender.get().getNickname())
                    .build();

            Optional<ChatRoom> chatRooms = chatRoomRepository.findByBoardIdAndOwnerNicknameAndSenderNickname(
                            board.getBoardId(), owner.get().getNickname(), sender.get().getNickname());
            if (chatRooms.isEmpty()) {
                chatRoomRepository.save(room);
                return new ResponseChatroom(ExceptionCode.CHATROOM_CREATE_OK, room);
            } else {
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
