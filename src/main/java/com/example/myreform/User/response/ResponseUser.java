package com.example.myreform.User.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUser {
    private int status; // enum
    private String code;
    private String description;
    private String token;
}
