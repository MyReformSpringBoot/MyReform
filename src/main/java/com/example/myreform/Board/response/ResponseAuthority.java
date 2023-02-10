package com.example.myreform.Board.response;

import com.example.myreform.validation.ExceptionCode;
import lombok.Getter;

@Getter
public class ResponseAuthority extends ResponseBoardEmpty {
    private boolean authority;

    public ResponseAuthority(ExceptionCode exceptionCode, boolean authority) {
        super(exceptionCode);
        this.authority = authority;
    }
}
