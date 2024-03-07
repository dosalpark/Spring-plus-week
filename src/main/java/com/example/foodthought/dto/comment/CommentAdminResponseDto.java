package com.example.foodthought.dto.comment;

import com.example.foodthought.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CommentAdminResponseDto {
    private String contents;
    private String userId;
    private Status status;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private List<CommentAdminResponseDto> replies;


    public void addReply(CommentAdminResponseDto reply) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        replies.add(reply);
    }
}
