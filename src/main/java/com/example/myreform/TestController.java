package com.example.myreform;

import com.example.myreform.domain.User;
import com.example.myreform.model.post.PostSaveDto;
import com.example.myreform.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@RestController
public class TestController {

    @Autowired
    PostService postService;

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }

    @PostMapping("/post")
    public PostSaveDto Save(@RequestBody ObjectNode saveObj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();   // JSON을 Object화 하기 위한 Jackson ObjectMapper 이용
        PostSaveDto postSaveDto = mapper.treeToValue(saveObj.get("post"), PostSaveDto.class);
        User user = mapper.treeToValue(saveObj.get("user"), User.class);

        System.out.println("postSaveDto = " + postSaveDto.getUser());
        System.out.println("postSaveDto = " + postSaveDto.getPost_id());
        postService.save(user, postSaveDto);
        return postSaveDto;
    }
}

