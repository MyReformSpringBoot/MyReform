package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.Chatroom;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Chatroom findByBoardIdAndSenderId(Long boardId, Long senderId);
    Chatroom save(ChatroomSaveDto chatroomSaveDto);
}

