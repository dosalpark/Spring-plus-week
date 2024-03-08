package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BoardReplyNotAllowedException extends RuntimeException {

    private ErrorCode errorCode;


    public BoardReplyNotAllowedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
