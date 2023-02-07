package com.example.myreform.Like.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseLike extends ResponseType {
    private Long countOfLike;

    public ResponseLike(ExceptionCode exceptionCode, Long countOfLike) {
        super(exceptionCode);
        this.countOfLike = countOfLike;
    }
}
