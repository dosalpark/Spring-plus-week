package com.example.foodthought.dto.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardResponseDto {
    private String title;
    private String author;
    private String publisher;
    private String image;
    private String category;
    private String userId;
    private String contents;
}

