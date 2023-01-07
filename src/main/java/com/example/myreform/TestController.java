package com.example.myreform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() throws Exception {

        return "MongoDB Test";
    }

}
