package com.example.foodthought.dto.book;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBookResponseDto {

    private String title;
    private String author;
    private String publisher;
    private String image;
    private String category;
    @Enumerated(value = EnumType.STRING)
    private LocalDateTime createdAt;
    @Enumerated(value = EnumType.STRING)
    private LocalDateTime modifiedAt;
}
