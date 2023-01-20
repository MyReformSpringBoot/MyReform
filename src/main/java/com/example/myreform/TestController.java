package com.example.myreform;

import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }
}

