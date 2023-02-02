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


//    @Getter
//    @Setter
//    class Save {
//
//    }
    // 게시물 생성
    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestPart(value = "saveObj") BoardSaveDto boardSaveDto, @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        //이미지가 들어가며 return type ResponseEntity<Post>에서 ResponseEntity<Object>로 바뀜
        //파라메터 @Requestbody ObjectNode saveObj => @RequestPart ObjectNode saveObj(json형식과 이미지파일 모두 받기 위해 고침)
        System.out.println("saveObj = " + boardSaveDto);
//        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
//        BoardSaveDto boardSaveDto = mapper.treeToValue(saveObj, BoardSaveDto.class);//nickname과 board정보 같이 맵핑

        return new ResponseEntity<>(boardService.save(boardSaveDto, files), HttpStatus.OK);
    }

    // 게시물 조회
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Object> getBoard(@RequestParam(required = false) Long lastBoardId, @RequestParam int size,
                                           @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String keyword) throws Exception {
        return new ResponseEntity<>(boardService.fetchBoardPagesBy(lastBoardId, size, categoryId, keyword), HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Object> getOneBoard(@PathVariable("boardId") long boardId) {
        return new ResponseEntity<>(boardService.getOneBoard(boardId), HttpStatus.OK);
    }

    //게시물 수정
    @PostMapping("/{boardId}")
    public ResponseEntity<Object> updatePost(@PathVariable("boardId") long boardId, @RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception{
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        BoardUpdateDto boardUpdateDto = mapper.treeToValue(saveObj, BoardUpdateDto.class);//nickname과 board정보 같이 맵핑
        return new ResponseEntity<>(boardService.update(boardId, boardUpdateDto, files), HttpStatus.OK);
    }

    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public Object deletePost(@PathVariable("boardId") long boardId, @RequestBody User user) {
        return new ResponseEntity<>(boardService.delete(boardId, user), HttpStatus.OK);
    }
}
