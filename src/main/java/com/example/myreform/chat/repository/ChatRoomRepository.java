package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByOwnerUserIdOrSenderUserId(String ownerUserId, String senderUserId);
    Optional<ChatRoom> findByBoardIdAndOwnerUserIdAndSenderUserId(Long boardId, String ownerUserId, String senderUserId);
    Object save(ChatRoom chatRoom);
    ChatRoom findByChatroomId(Long chatroomId);
}

