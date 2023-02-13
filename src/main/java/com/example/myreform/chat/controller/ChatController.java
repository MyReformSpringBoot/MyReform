package com.example.myreform.chat.controller;

import com.example.myreform.chat.dto.ChatroomFindDto;
import com.example.myreform.chat.dto.ChatroomSaveDto;
import com.example.myreform.chat.dto.MessageFindDto;
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
    @PostMapping("/chats/create")
    public Object createRoom(@RequestBody ChatroomSaveDto chatroomSaveDto) {
        return new ResponseEntity<>(chatService.save(chatroomSaveDto), HttpStatus.OK);
    }

    // 채팅방 리스트 조회 : 임시로 post
    @PostMapping("/chats")
    public Object findRoom(@RequestBody ChatroomFindDto chatroomFindDto) {
        return new ResponseEntity<>(chatService.findByNickname(chatroomFindDto), HttpStatus.OK);
    }

    // 채팅 기록
    @GetMapping("/messages")
    public Object findMsg(@RequestBody MessageFindDto messageFindDto) {
        return new ResponseEntity<>(chatService.findMessages(messageFindDto), HttpStatus.OK);
    }
}