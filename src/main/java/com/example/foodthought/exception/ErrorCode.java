package com.example.foodthought.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public enum ErrorCode {

    NOT_FOUND_BOOK(HttpStatus.BAD_REQUEST,"해당하는 책이 없습니다."),
    NOT_FOUND_SEARCH_TITLE_BOOK(HttpStatus.BAD_REQUEST," - 검색결과가 없습니다."),
    NOT_FOUND_SEARCH_BOOK(HttpStatus.BAD_REQUEST,"등록된 책이 없습니다."),
    REPLY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "댓글이 달린 게시물에만 대댓글을 달 수 있습니다."),
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST,"해당하는 게시물이 없습니다."),
    NOT_FOUND_SEARCH_BOARD(HttpStatus.BAD_REQUEST,"등록된 게시물이 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST,"해당하는 댓글이 없습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"해당하는 이용자가 없습니다."),
    NOT_FOUND_IMAGE(HttpStatus.BAD_REQUEST,"이미지를 업로드 해주세요"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN,"권한이 없습니다."),
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST,"본인을 Follow 할 수 없습니다."),
    COMMENT_REPLY_NOT_ALLOWED(HttpStatus.BAD_REQUEST,"대댓글에는 댓글을 달 수 없습니다.");


    private final HttpStatus status;
    private String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setMessage(String message, String add){
        this.message = add+message;
    }
}
