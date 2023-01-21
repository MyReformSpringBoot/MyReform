package com.example.myreform.User.validation;

public enum ExceptionCode {
    LOGIN_OK(200, "B000", "로그인 성공"),
    SIGNUP_CREATED(201, "A000", "회원가입 성공"),
    LOGIN_ID(404, "B001", "로그인 실패"),
    LOGIN_PW(404, "B002", "로그인 실패"),
    SIGNUP_ID(409, "A001", "ID 중복"),
    SIGNUP_NICKNAME(409, "A002", "NICKNAME 중복");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
