package com.example.foodthought.exception.customException;

import com.example.foodthought.exception.ErrorCode;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ImageNotFoundException extends IOException {

    private ErrorCode errorCode;


    public ImageNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
