package com.example.foodthought.dto.like;

import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class LikeTopResponseDto {
     private Long boardId;
     private String booktitle;
     private String bookauthor;
     private String bookpublisher;
     private String bookimage;
     private String bookcategory;
     private String username;
     private String contents;
     private Long countLikes;
}
