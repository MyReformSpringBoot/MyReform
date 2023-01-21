package com.example.myreform.User.controller;

import com.example.myreform.User.dto.UserLoginDto;
import com.example.myreform.User.dto.UserSignupDto;
import com.example.myreform.User.response.ResponseUser;
import com.example.myreform.User.service.UserService;
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
    public ResponseUser signUp(@RequestBody UserSignupDto request) {
        System.out.println("<sign up>");
        System.out.println(request.getNickname() + ", " + request.getId() + ", "
                + request.getPw() + ", " + request.getEmail()+  ", " + request.getMarketing() + "\n");
        return userService.signUp(request);
    }

    @PostMapping("/users")
    public ResponseUser login(@RequestBody UserLoginDto request) {
        System.out.println("<login>");
        System.out.println(request.getId() + ", " + request.getPw() + "\n");
        return userService.login(request);
    }
}
