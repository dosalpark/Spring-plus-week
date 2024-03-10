package com.example.foodthought.dto.book;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateBookRequestDto {
    @NotBlank(message = "책 이름을 입력해주세요.")
    private String title;
    @NotBlank(message = "저자를 입력해주세요.")
    private String author;
    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;
    @NotBlank(message = "카테고리를 입력해주세요.")
    private String category;
}