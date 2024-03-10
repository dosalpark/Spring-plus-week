package com.example.foodthought.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {
    @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
    private String contents;
}
