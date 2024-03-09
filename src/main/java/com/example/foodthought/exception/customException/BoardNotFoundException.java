package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BoardNotFoundException extends RuntimeException {

    private ErrorCode errorCode;


    public BoardNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
