package com.example.myreform.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse { // extends ResponseEntity
    //private StatusEnum status;
    private int code;
    private String description;
    private UserDTO data;
}


