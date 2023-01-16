package com.example.myreform;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }

}

