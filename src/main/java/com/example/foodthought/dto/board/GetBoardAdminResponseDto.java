package com.example.foodthought.dto.board;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBoardAdminResponseDto {
    private String title;
    private String author;
    private String publisher;
    private String image;
    private String category;
    private String contents;
    private String Status;
}

