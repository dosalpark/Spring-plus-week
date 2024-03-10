package com.example.foodthought.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String contents;
}
