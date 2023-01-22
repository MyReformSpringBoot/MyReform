package com.example.myreform.Board.response;

public class ResponseBoard {
    private final int status;
    private final String code;
    private final String message;
    private final Object data;

    public ResponseBoard(int status, String code, String message, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
