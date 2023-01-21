package com.example.myreform.User.validation;

import org.springframework.http.HttpStatus;


import static org.springframework.http.HttpStatus.OK; //200
import static org.springframework.http.HttpStatus.CREATED; //201
import static org.springframework.http.HttpStatus.NOT_FOUND; //404
import static org.springframework.http.HttpStatus.CONFLICT; //409
public enum ExceptionCode {
    LOGIN_OK(OK, "B000", "로그인 성공"),
    SIGNUP_CREATED(CREATED, "A000", "회원가입 성공"),
    LOGIN_ID(NOT_FOUND, "B001", "로그인 실패"),
    LOGIN_PW(NOT_FOUND, "B002", "로그인 실패"),
    SIGNUP_ID(CONFLICT, "A001", "ID 중복"),
    SIGNUP_NICKNAME(CONFLICT, "A002", "NICKNAME 중복");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
