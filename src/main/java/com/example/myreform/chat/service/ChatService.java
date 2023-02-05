package com.example.myreform.chat.service;

import com.example.myreform.Board.domain.Board;
import com.example.myreform.Board.repository.BoardRepository;
import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.chat.domain.Chatroom;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import com.example.myreform.chat.repository.ChatroomRepository;
import com.example.myreform.chat.repository.UserBoardChatroomRepository;
import com.example.myreform.chat.response.ResponseChatroom;
import com.example.myreform.chat.domain.UserBoardChatroom;
import com.example.myreform.chat.response.ResponseChatroomEmpty;
import com.example.myreform.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService implements ChatServiceImpl {

    private final ChatroomRepository chatroomRepository;
    private final UserBoardChatroomRepository userBoardChatroomRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    //private final MessageRepository messageRepository;

    @Override // 채팅방 생성 및 조회
    public Object save(ChatroomSaveDto chatroomSaveDto) {
        Optional<User> sender = userRepository.findByNickname(chatroomSaveDto.getSenderNickname());
        Optional<User> owner = userRepository.findByNickname(chatroomSaveDto.getOwnerNickname());
        Board board = boardRepository.findBoardByBoardIdAndStatusEquals(chatroomSaveDto.getBoardId(), 1);

        if (owner.isPresent() && sender.isPresent()) {
            UserBoardChatroom roomInfo = UserBoardChatroom.builder()
                    .board(board).owner(owner.get()).sender(sender.get()).build();
            System.out.println(roomInfo);

            Chatroom room = Chatroom.builder()
                    .boardId(chatroomSaveDto.getBoardId())
                    .ownerId(roomInfo.getOwner().getUserId())
                    .senderId(roomInfo.getSender().getUserId())
                    .build();
            System.out.println(room);

            ResponseChatroom responseChatroom = checkChatroom(room);
            System.out.println(responseChatroom);
            if (responseChatroom == null) {
                userBoardChatroomRepository.save(roomInfo);
                chatroomRepository.save(room);
                return new ResponseChatroom(ExceptionCode.CHATROOM_CREATE_OK, room);
            }
            else {
                return responseChatroom;
            }
        }
        else {
            return new ResponseChatroomEmpty(ExceptionCode.CHATROOM_UPDATE_INVALID);
        }
    }


    private ResponseChatroom checkChatroom(Chatroom room) {
        Chatroom chatroom = chatroomRepository.findByBoardIdAndSenderId(room.getBoardId(), room.getSenderId());
        if (chatroom == null) {
            return null;
        }
        return new ResponseChatroom(ExceptionCode.CHATROOM_CREATE_ERROR, chatroom.getChatroomId());
    }


}
