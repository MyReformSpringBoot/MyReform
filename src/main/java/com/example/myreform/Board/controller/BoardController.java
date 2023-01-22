package com.example.myreform.Board.controller;


import com.example.myreform.Board.dto.BoardSaveDto;
import com.example.myreform.Board.response.ResponseBoard;
import com.example.myreform.Board.service.BoardService;
import com.example.myreform.User.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    public ResponseEntity<Object> save(@RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        //이미지가 들어가며 return type ResponseEntity<Post>에서 ResponseEntity<Object>로 바뀜
        //파라메터 @Requestbody ObjectNode saveObj => @RequestPart ObjectNode saveObj(json형식과 이미지파일 모두 박디 위해 고침)

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        BoardSaveDto boardSaveDto = mapper.treeToValue(saveObj.get("board"), BoardSaveDto.class);
        User user = mapper.treeToValue(saveObj.get("user"), User.class);

        return new ResponseEntity<>(boardService.save(user, boardSaveDto, files), HttpStatus.OK);
    }

    // 게시물 조회
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Object> getBoard(@RequestParam Long lastBoardId, @RequestParam int size,
                                           @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String keyword) throws Exception {
        return new ResponseEntity<>(boardService.fetchBoardPagesBy(lastBoardId, size, categoryId, keyword), HttpStatus.OK);
    }

    //게시물 수정
    @PostMapping("/{boardId}")
    public ResponseEntity<Object> updatePost(@PathVariable("boardId") long boardId, @RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception{
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        BoardSaveDto boardSaveDto = mapper.treeToValue(saveObj.get("board"), BoardSaveDto.class);
        User user = mapper.treeToValue(saveObj.get("user"), User.class);
        return new ResponseEntity<>(boardService.update(boardId, boardSaveDto, user, files), HttpStatus.OK);
    }

    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public Object deletePost(@PathVariable("boardId") long boardId, @RequestBody User user) {
        return new ResponseEntity<>(boardService.delete(boardId, user), HttpStatus.OK);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
    }
}
