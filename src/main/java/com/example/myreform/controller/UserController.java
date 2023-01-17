package com.example.myreform.controller;

import com.example.myreform.domain.UserDTO;
import com.example.myreform.domain.UserResponse;
import com.example.myreform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    public String test() {
        return "MyReform users page!";
    }

    // 회원가입
    @PostMapping("/users/new-user")
    public UserResponse signUp(@RequestBody UserDTO request) {
        System.out.println("<sign up>");
        System.out.println(request.getNickname() + ", " + request.getId() + ", "
                + request.getPw() + ", " + request.getEmail()+  ", " + request.getMarketing() + "\n");
        return userService.signUp(request);
    }

    @PostMapping("/users")
    public UserResponse login(@RequestBody UserDTO request) {
        System.out.println("<sign up>");
        System.out.println(request.getNickname() + ", " + request.getId() + ", "
                + request.getPw() + ", " + request.getEmail()+  ", " + request.getMarketing() + "\n");
        return userService.login(request);
    }
}
