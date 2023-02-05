package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.UserBoardChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserBoardChatroomRepository extends JpaRepository<UserBoardChatroom, Long> {
    UserBoardChatroom save(UserBoardChatroom userBoardChatroom);
}

