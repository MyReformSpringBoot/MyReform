package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
}

