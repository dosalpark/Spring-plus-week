package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookSearchNotFoundException extends RuntimeException {


    private ErrorCode errorCode;
    private String title;


    public BookSearchNotFoundException(ErrorCode errorCode, String title) {
        this.errorCode = errorCode;
        this.title = title;
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}