package com.example.myreform.Board.response;

import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseBoard extends ResponseBoardEmpty {
    private Object data;

    public ResponseBoard(ExceptionCode exceptionCode, Object data) {
        super(exceptionCode);
        this.data = data;
    }
}
