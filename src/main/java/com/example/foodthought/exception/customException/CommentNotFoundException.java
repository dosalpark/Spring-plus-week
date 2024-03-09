package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private ErrorCode errorCode;


    public CommentNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
