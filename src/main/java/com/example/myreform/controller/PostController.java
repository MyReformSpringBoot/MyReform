package com.example.myreform.controller;


import com.example.myreform.service.PostServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class PostController {

    private final PostServiceImpl postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public void createPost(int userId, int categoryId, String title, String contents, @RequestPart(value = "file") List<MultipartFile> files) throws Exception {
        postService.createPost(userId, categoryId, title, contents, files);
    }

//    @PostMapping("/{boardId}")
//    public void updatePost(long postId){
//        postService.updatePost(postId);
//    }
//    @DeleteMapping("/{boardId}")
//    public void deletePost(long postId){
//        postService.deletePost(postId);
//    }
}
