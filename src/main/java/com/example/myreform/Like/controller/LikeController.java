package com.example.myreform.Like.controller;

import com.example.myreform.Like.Service.LikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    LikeServiceImpl likeServiceImpl;
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Object> addLike(@RequestParam("boardId") Long boardId, @RequestParam("token") String loginId) {
        return new ResponseEntity<>(likeServiceImpl.addLike(boardId, loginId), HttpStatus.OK);
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Object> removeLike(@RequestParam("boardId") Long boardId, @RequestParam("token") String loginId) {
        return new ResponseEntity<>(likeServiceImpl.removeLike(boardId, loginId), HttpStatus.OK);
    }

}
