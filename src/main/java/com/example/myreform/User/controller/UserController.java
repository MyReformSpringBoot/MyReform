package com.example.myreform.User.controller;

import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.User.dto.UserUpdateDto;
import com.example.myreform.User.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/home")
    public String test() {
        return "MyReform page!";
    }

    // 회원가입
    @PostMapping("/users/new-user")
    public ResponseEntity<Object> signUp(@RequestBody UserSignupDto userSignupDto) {
        return new ResponseEntity<>(userService.signUp(userSignupDto), HttpStatus.OK);
    }

    //로그인
    @PostMapping("/users")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginDto) {
        return new ResponseEntity<>(userService.login(userLoginDto), HttpStatus.OK);
    }

    //프로필
    @GetMapping("/users/{loginId}/profiles")
    public ResponseEntity<Object> find(@PathVariable("loginId") String loginId) {
        return new ResponseEntity<>(userService.find(loginId), HttpStatus.OK);
    }

    @PostMapping("/users/{loginId}/profiles")
    public ResponseEntity<Object> update(@PathVariable("loginId") String loginId, @RequestBody UserUpdateDto userUpdateDto) throws Exception{
        return new ResponseEntity<>(userService.update(loginId, userUpdateDto), HttpStatus.OK);
    }

}
