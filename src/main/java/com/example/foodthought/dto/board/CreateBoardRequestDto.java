package com.example.foodthought.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBoardRequestDto {
    @NotNull(message = "책 번호를 입력해주세요.")
    @Positive(message = "책 번호는 1부터 시작합니다.")
    private Long bookId;
    @NotBlank(message = "한줄평을 입력해주세요.")
    private String contents;
}
