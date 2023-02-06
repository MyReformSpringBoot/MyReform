package com.example.myreform.chat.service;

import com.example.myreform.User.domain.User;
import com.example.myreform.chat.dto.ChatroomFindDto;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatServiceImpl {
    Object save(ChatroomSaveDto ChatroomSaveDto);
    Object findByNickname(ChatroomFindDto chatroomFindDto);
}
