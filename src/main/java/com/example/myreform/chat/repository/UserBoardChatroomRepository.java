package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.UserBoardChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserBoardChatroomRepository extends JpaRepository<UserBoardChatroom, Long> {
    UserBoardChatroom save(UserBoardChatroom userBoardChatroom);
    List<UserBoardChatroom> findByOwnerNicknameOrSenderNickname(String ownerNickname, String senderNickname);
}

