package com.example.myreform.chat.repository;

import com.example.myreform.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByOwnerNicknameOrSenderNickname(String ownerNickname, String senderNickname);
    Optional<ChatRoom> findByBoardIdAndOwnerNicknameAndSenderNickname(Long boardId, String ownerNickname, String senderNickname);
    ChatRoom save(ChatRoom chatRoom);
    ChatRoom findByChatroomId(Long chatroomId);
}

