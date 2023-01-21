package com.example.myreform.validation;

import static com.example.myreform.validation.HttpStatus.*;

public enum ExceptionCode {
    LOGIN_OK(Success, "B000", "로그인 성공"),
    SIGNUP_CREATED(CREATED, "A000", "회원가입 성공"),
    LOGIN_ID(FAIL_LOGIN, "B001", "로그인 실패"),
    LOGIN_PW(FAIL_LOGIN, "B002", "로그인 실패"),
    SIGNUP_ID(DUPLICATED_VALUE, "A001", "ID 중복"),
    SIGNUP_NICKNAME(DUPLICATED_VALUE, "A002", "NICKNAME 중복");

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
