package com.example.myreform.validation;

import lombok.Getter;

@Getter
public enum HttpStatus {
    /**
     * 200: 요청 성공
     */
    Success(200),
    CREATED(201),
    FAIL_LOGIN(404),
    DUPLICATED_VALUE(409);

    public int value;

    HttpStatus(int value) {
        this.value = value;
    }
}
