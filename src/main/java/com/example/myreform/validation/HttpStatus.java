package com.example.myreform.validation;

import lombok.Getter;

@Getter
public enum HttpStatus {
    /**
     * 200: 요청 성공
     */
    SUCCESS(200),
    CREATED(201),
    NOT_FOUND_VALUE(404),
    DUPLICATED_VALUE(409),
    INVALID_ACCESS(403);

    public int value;

    HttpStatus(int value) {
        this.value = value;
    }
}
