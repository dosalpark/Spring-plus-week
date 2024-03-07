package com.example.foodthought.dto.comment;

import com.example.foodthought.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CommentResponse {
    private String contents;
    private String userId;
    private List<CommentResponse> replies;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public void addReply(CommentResponse reply) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        replies.add(reply);
    }
}
