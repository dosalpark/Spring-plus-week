package com.example.foodthought.dto.board;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateBoardRequestDto {
    private Long bookId;
    private String contents;
}
