package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.comment.CommentResponseDto;
import com.example.foodthought.dto.comment.CreateCommentRequestDto;
import com.example.foodthought.dto.comment.UpdateCommentRequest;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentController {


    private final CommentService commentService;


    //댓글 생성
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<ResponseDto<Boolean>> createParentComment(
            @PathVariable Long boardId,
            @RequestBody CreateCommentRequestDto createCommentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createParentComment(
                boardId,
                createCommentRequestDto,
                userDetails.getUser()));
    }


    // 대댓글 생성
    @PostMapping("/{boardId}/comments/{parentCommentId}/replies")
    public ResponseEntity<ResponseDto<Boolean>> createChildComment(
            @PathVariable Long boardId,
            @PathVariable Long parentCommentId,
            @RequestBody CreateCommentRequestDto createCommentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createChildComment(
                boardId,
                parentCommentId,
                createCommentRequestDto,
                userDetails.getUser()));
    }


    // 댓글 조회
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<ResponseDto<List<CommentResponseDto>>> getComment(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "createAt") String sort,
            @RequestParam(defaultValue = "true") boolean isAsc) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(boardId, page, size, sort, isAsc));
    }


    // 댓글 수정
    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<Boolean>> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest updateCommentRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(
                boardId,
                commentId,
                updateCommentRequest,
                userDetails.getUser()));
    }


    // 대댓글 수정
    @PutMapping("/{boardId}/comments/{parentCommentId}/replies/{replyId}")
    public ResponseEntity<ResponseDto<Boolean>> updateReply(
            @PathVariable Long boardId,
            @PathVariable Long parentCommentId,
            @PathVariable Long replyId,
            @RequestBody UpdateCommentRequest updateCommentRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateReply(
                boardId,
                parentCommentId,
                replyId,
                updateCommentRequest,
                userDetails.getUser()));
    }


    // 댓글 삭제
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(
                boardId,
                commentId,
                userDetails.getUser()));
    }


    // 대댓글 삭제
    @DeleteMapping("/{boardId}/comments/{parentCommentId}/replies/{replyId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteReply(
            @PathVariable Long boardId,
            @PathVariable Long parentCommentId,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteReply(
                boardId,
                parentCommentId,
                replyId,
                userDetails.getUser()));
    }
}
