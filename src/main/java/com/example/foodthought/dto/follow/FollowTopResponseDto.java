package com.example.foodthought.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class FollowTopResponseDto {
     private Long id;
     private String userId;
     private String username;
     private String intro;
     private String userPhoto;
     private Long countFollows;
}
