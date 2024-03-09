package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {


    private ErrorCode errorCode;


    public BookNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}