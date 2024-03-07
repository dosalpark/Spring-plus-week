package com.example.foodthought.dto.book;

import lombok.Getter;

@Getter
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private String category;
}
