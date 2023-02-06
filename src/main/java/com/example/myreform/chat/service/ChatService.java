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
        Board board = boardRepository.findBoardByBoardIdAndStatusEquals(chatroomSaveDto.getBoardId(), 1);
        Optional<User> sender = userRepository.findByNickname(chatroomSaveDto.getSenderNickname());
        Optional<User> owner = userRepository.findByNickname(board.getUser().getNickname());

        if (validation(board, owner, sender)) {
            Chatroom room = Chatroom.builder()
                    .boardId(chatroomSaveDto.getBoardId())
                    .ownerId(owner.get().getUserId())
                    .senderId(sender.get().getUserId())
                    .build();

            UserBoardChatroom roomInfo = UserBoardChatroom.builder()
                    .boardTitle(board.getTitle()).owner(owner.get().getNickname()).sender(sender.get().getNickname()).build();

            ResponseChatroom responseChatroom = checkChatroom(room);
            if (responseChatroom == null) {
                chatroomRepository.save(room);
                roomInfo.setLastTime(room.getCreateAt());
                roomInfo.setChatroomId(room.getChatroomId());
                userBoardChatroomRepository.save(roomInfo);
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

    private boolean validation(Board board, Optional<User> owner, Optional<User> sender) {
        if (board == null || owner.isEmpty() || sender.isEmpty()) {
            //System.out.println("빈 정보");
            return false;
        }
        else if (owner.get().getUserId().equals(sender.get().getUserId())) {
            //System.out.println("중복");
            return false;
        }
        else {
            return true;
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
