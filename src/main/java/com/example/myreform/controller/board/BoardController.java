package com.example.myreform.controller.board;

import com.example.myreform.domain.user.User;
import com.example.myreform.domain.board.BoardFindDto;
import com.example.myreform.domain.board.BoardSaveDto;
import com.example.myreform.service.board.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 전체 게시물 조회
    @GetMapping("")
    @ResponseBody
    public List<BoardFindDto> getPost(@RequestParam Long lastBoardId, @RequestParam int size) throws Exception {
        return boardService.fetchBoardPagesBy(lastBoardId, size);
    }

    //게시물 수정
    @PostMapping("/{boardId}")
    public ResponseEntity<Object> updatePost(@PathVariable("boardId") long boardId, @RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception{
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        BoardSaveDto postSaveDto = mapper.treeToValue(saveObj.get("board"), BoardSaveDto.class);

        return new ResponseEntity<>(boardService.update(boardId, postSaveDto, files), HttpStatus.OK);
    }

    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public String deletePost(@PathVariable("boardId") long boardId){

        return boardService.delete(boardId);
    }
}
