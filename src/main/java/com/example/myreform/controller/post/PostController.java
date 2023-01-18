package com.example.myreform.controller.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostFindDto;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class PostController {
    @Autowired
    PostService postService;

    // 게시물 생성
    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        //이미지가 들어가며 return type ResponseEntity<Post>에서 ResponseEntity<Object>로 바뀜
        //파라메터 @Requestbody ObjectNode saveObj => @RequestPart ObjectNode saveObj(json형식과 이미지파일 모두 박디 위해 고침)

        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        PostSaveDto postSaveDto = mapper.treeToValue(saveObj.get("post"), PostSaveDto.class);
        User user = mapper.treeToValue(saveObj.get("user"), User.class);

        return new ResponseEntity<>(postService.save(user, postSaveDto, files), HttpStatus.OK);
    }

    // 전체 게시물 조회
    @GetMapping("")
    @ResponseBody
    public List<PostFindDto> getPost(@RequestParam Long lastPostId, @RequestParam int size) throws Exception {
        return postService.fetchPostPagesBy(lastPostId, size);
    }

    //게시물 수정
    @PostMapping("/{boardId}")
    public ResponseEntity<Object> updatePost(@PathVariable("boardId") long postId, @RequestPart ObjectNode saveObj, @RequestPart(value = "file") List<MultipartFile> files) throws Exception{
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        PostSaveDto postSaveDto = mapper.treeToValue(saveObj.get("post"), PostSaveDto.class);

        return new ResponseEntity<>(postService.update(postId, postSaveDto, files), HttpStatus.OK);
    }

    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public String deletePost(@PathVariable("boardId") long postId){

        return postService.delete(postId);
    }
}
