package com.example.myreform.chat.controller;

import com.example.myreform.chat.dto.ChatroomFindDto;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import com.example.myreform.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    
    // 채팅방 생성
    @PostMapping("/chats")
    public Object createRoom(@RequestBody ChatroomSaveDto chatroomSaveDto) {
        return new ResponseEntity<>(chatService.save(chatroomSaveDto), HttpStatus.OK);
    }

    // 채팅방 조회
    @GetMapping("/chats")
    public Object findRoom(@RequestBody ChatroomFindDto chatroomFindDto) {
        return new ResponseEntity<>(chatService.findByNickname(chatroomFindDto), HttpStatus.OK);
    }

}