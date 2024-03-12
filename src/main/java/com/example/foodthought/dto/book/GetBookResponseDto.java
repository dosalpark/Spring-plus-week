package com.example.foodthought.dto.book;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetBookResponseDto {

    private String title;
    private String author;
    private String publisher;
    private String image;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
