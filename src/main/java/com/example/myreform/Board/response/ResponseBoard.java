package com.example.myreform.Board.response;

import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseBoard {
    private final int status;
    private final String code;
    private final String message;
    private final Object data;

    public ResponseBoard(ExceptionCode exceptionCode, Object data) {
        this.status = exceptionCode.getStatus().getValue();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
        this.data = data;
    }
}
