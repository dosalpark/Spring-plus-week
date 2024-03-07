package com.example.foodthought.dto.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUsersResponseDto {
    private Long id;
    private String userId;
    private String username;
    private String intro;
    private String userPhoto;
}
