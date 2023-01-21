package com.example.myreform.validation;

import static com.example.myreform.validation.HttpStatus.*;

public enum ExceptionCode {
    /**
     * 회원가입 및 로그인
     */
    LOGIN_OK(SUCCESS, "B000", "로그인 성공"),
    SIGNUP_CREATED(CREATED, "A000", "회원가입 성공"),
    LOGIN_ID(NOT_FOUND_VALUE, "B001", "로그인 실패"),
    LOGIN_PW(NOT_FOUND_VALUE, "B002", "로그인 실패"),
    SIGNUP_ID(DUPLICATED_VALUE, "A001", "ID 중복"),
    SIGNUP_NICKNAME(DUPLICATED_VALUE, "A002", "NICKNAME 중복"),


    /**
     * 게시물 포스팅
     */
    BOARD_GET_OK(SUCCESS, "C000", "게시물"),
    BOARD_NOT_FOUND(NOT_FOUND_VALUE, "C001", "게시물을 찾을 수 없습니다."),
    BOARD_CREATE_OK(SUCCESS, "D000", "게시물 작성에 성공했습니다."),
    BOARD_CREATE_ERROR(SUCCESS, "D001", "게시물 작성에 실패했습니다."), // NOT NULL 정보를 기입하도록 서버에서 처리할지 프론트엔드에서 처리할지 합의해야함
    BOARD_DELETE_OK(SUCCESS, "E000", "게시물을 삭제했습니다."),
    BOARD_DELETE_INVALID(INVALID_ACCESS, "E001", "게시물을 삭제할 수 없습니다."),
    BOARD_UPDATE_OK(SUCCESS, "F000", "게시물 업데이트에 성공했습니다."),
    BOARD_UPDATE_INVALID(INVALID_ACCESS, "F001", "게시물 업데이트에 성공하지 못했습니다.");


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
