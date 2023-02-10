package com.example.myreform.Board.controller;


import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.service.BoardService;
import com.example.myreform.User.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    BoardService boardService;

    // 게시물 생성
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Object> save(@RequestPart(value = "saveObj") BoardSaveDto boardSaveDto, @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        System.out.println("boardSaveDto = " + boardSaveDto);
        return new ResponseEntity<>(boardService.save(boardSaveDto, files), HttpStatus.OK);
    }

    // 게시물 조회
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Object> getBoard(@RequestParam(required = false) Long lastBoardId, @RequestParam int size,
                                           @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String keyword, @RequestParam String loginNickname) throws Exception {
        return new ResponseEntity<>(boardService.fetchBoardPagesBy(lastBoardId, size, categoryId, keyword,loginNickname), HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> getOneBoard(@PathVariable("boardId") long boardId, @RequestParam("nickname") String loginNickname) {
        return new ResponseEntity<>(boardService.getOneBoard(boardId, loginNickname), HttpStatus.OK);
    }

    //게시물 수정
    @PostMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> updatePost(@PathVariable("boardId") long boardId, @RequestPart(value = "saveObj") BoardUpdateDto boardUpdateDto, @RequestPart(value = "file") List<MultipartFile> files) throws Exception{
        return new ResponseEntity<>(boardService.update(boardId, boardUpdateDto, files), HttpStatus.OK);
    }

    //게시물 삭제
    @PostMapping("/delete/{boardId}")
    @ResponseBody
    public Object deletePost(@PathVariable("boardId") long boardId, @RequestBody User user) {
        return new ResponseEntity<>(boardService.delete(boardId, user), HttpStatus.OK);
    }
    //게시물 권한 확인
    @PostMapping("/{boardId}/{token}")
    @ResponseBody
    public Object checkAuthority(@PathVariable("boardId") long boardId, @PathVariable("token") String LoginNickname) {
        //return boardService.checkAuthority(boardId, LoginNickname);
        return new ResponseEntity<>(boardService.checkAuthority(boardId, LoginNickname), HttpStatus.OK);
    }
}
