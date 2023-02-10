package com.example.myreform.chat.service;

import com.example.myreform.chat.domain.ChatRoom;
import com.example.myreform.chat.dto.ChatroomFindDto;
import com.example.myreform.chat.dto.ChatroomSaveDto;

public interface ChatServiceImpl {
    Object findChatroomById(Long roomId);
    Object save(ChatroomSaveDto ChatroomSaveDto);
    Object findByNickname(ChatroomFindDto chatroomFindDto);
}
