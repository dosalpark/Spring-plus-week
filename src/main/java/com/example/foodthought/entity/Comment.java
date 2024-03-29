package com.example.foodthought.entity;

import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.comment.UpdateCommentRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;


    @Lob
    private String contents;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.POST;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_Id", nullable = false)
    private Board board;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_Id")
    private Comment parentComment;


    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    private List<Comment> replies;


    public Comment(UpdateCommentRequest request, Board board, User user) {
        this.contents = request.getContents();
        this.board = board;
        this.user = user;
    }


    public void updateStatusComment(UpdateStatusRequestDto updateStatusRequestDto) {
        String upperStatus = (updateStatusRequestDto.getStatus()).toUpperCase();
        this.status = Status.valueOf(upperStatus);
    }


    public void updateComment(UpdateCommentRequest updateCommentRequest) {
        this.contents = updateCommentRequest.getContents();
    }
}
