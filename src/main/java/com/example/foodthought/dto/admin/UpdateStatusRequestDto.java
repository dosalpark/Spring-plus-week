package com.example.foodthought.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateStatusRequestDto {
    @NotBlank(message = "상태를 입력해주세요")
    private String status;
}
