package com.example.myreform.validation;

import java.util.Arrays;

import static com.example.myreform.validation.HttpStatus.*;

public enum ExceptionCode {
    /**
     * 회원가입 및 로그인
     */
    SIGNUP_CREATED_OK(CREATED, "A000", "회원가입 성공"),
    SIGNUP_DUPLICATED_ID(DUPLICATED_VALUE, "A001", "ID 중복"),
    SIGNUP_DUPLICATED_NICKNAME(DUPLICATED_VALUE, "A002", "NICKNAME 중복"),
    LOGIN_OK(SUCCESS, "B000", "로그인 성공"),
    LOGIN_NOT_FOUND_ID(NOT_FOUND_VALUE, "B001", "로그인 실패"),
    LOGIN_NOT_FOUND_PW(NOT_FOUND_VALUE, "B002", "로그인 실패"),



    /**
     * 게시물 포스팅
     */
    BOARD_GET_OK(SUCCESS, "C000", "게시물"),
    BOARD_NOT_FOUND(NOT_FOUND_VALUE, "C001", "게시물을 찾을 수 없습니다."),
    BOARD_CREATE_OK(SUCCESS, "D000", "게시물 작성에 성공했습니다."),
    BOARD_CREATE_ERROR(SUCCESS, "D001", "게시물 작성에 실패했습니다."),
    BOARD_DELETE_OK(SUCCESS, "E000", "게시물을 삭제했습니다."),
    BOARD_DELETE_INVALID(INVALID_ACCESS, "E001", "게시물을 삭제할 수 있는 권한이 없습니다."),
    BOARD_UPDATE_OK(SUCCESS, "F000", "게시물 업데이트에 성공했습니다."),
    BOARD_UPDATE_INVALID(INVALID_ACCESS, "F001", "게시물을 변경할 수 있는 권한이 없습니다."),

    /**
     * 이미지
     */
    IMAGE_GET_OK(SUCCESS, "G000", "이미지"),
    IMAGE_NOT_FOUND(NOT_FOUND_VALUE, "G001", "이미지를 찾을 수 없습니다."),



    /**
     * 회원정보
     */

    USER_GET_OK(SUCCESS, "H000", "회원정보있음"),
    USER_NOT_FOUND(NOT_FOUND_VALUE, "H001", "회원정보없음"),
    USER_UPDATE_OK(SUCCESS, "I000", "회원정보수정성공"),
    USER_UPDATE_INVALID(NOT_FOUND_VALUE, "I001", "회원정보수정실패"),


    /**
     * 잘못된 ExceptionCode
     */
    EMPTY(null, "", "");

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

    public static ExceptionCode findExceptionCodeByCode(String code) {
        return Arrays.stream(ExceptionCode.values())
                .filter(x -> x.getCode().equals(code))
                .findFirst()
                .orElse(EMPTY);
    }
}
