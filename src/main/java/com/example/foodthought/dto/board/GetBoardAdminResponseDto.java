package com.example.foodthought.dto.board;


import com.example.foodthought.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardAdminResponseDto {
    private String title;
    private String author;
    private String publisher;
    private String image;
    private String category;
    private String userId;
    private String contents;
    @Enumerated(value = EnumType.STRING)
    private Status status;
}

