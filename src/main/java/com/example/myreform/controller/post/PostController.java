package com.example.myreform.controller.post;

import com.example.myreform.domain.Post;
import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    @Autowired
    PostService postService;

    // 게시물 생성
    @PostMapping("/boards/create")
    @ResponseBody
    public Post save(@RequestBody ObjectNode saveObj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        PostSaveDto postSaveDto = mapper.treeToValue(saveObj.get("post"), PostSaveDto.class);
        User user = mapper.treeToValue(saveObj.get("user"), User.class);
        return postService.save(user, postSaveDto);
    }


}
