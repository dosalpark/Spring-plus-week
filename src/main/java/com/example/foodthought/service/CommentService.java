package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.comment.CommentRequest;
import com.example.foodthought.dto.comment.CommentResponse;
import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Comment;
import com.example.foodthought.entity.User;
import com.example.foodthought.repository.BoardRepository;
import com.example.foodthought.repository.CommentRepository;
import com.example.foodthought.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    public ResponseDto createComment(Long boardId, Long parentCommentId, String contents, User user) {
        Board board = findBoard(boardId);
        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        }
        Comment comment = Comment.builder()
                .contents(contents)
                .board(board)
                .user(user)
                .parentComment(parentComment)
                .build();
         commentRepository.save(comment);
         return ResponseDto.success(HttpStatus.CREATED.value());
    }


    // 댓글, 대댓글 조회
    public List<CommentResponse> getComment(Long boardId) {
        findBoard(boardId);
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        return convertToDtoList(commentList);
    }


    // 댓글 수정
    @Transactional
    public ResponseDto updateComment(Long boardId, Long commentId, CommentRequest commentRequest, User user) {
        findBoard(boardId);
        User findUser = findUser(user);
        Comment comment = findComment(commentId);

        if (!comment.getUser().equals(findUser)) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        comment.updateComment(commentRequest);
        return ResponseDto.success(HttpStatus.CREATED.value());
    }


    // 대댓글 수정
    @Transactional
    public ResponseDto updateReply(Long boardId, Long parentCommentId, Long replyId, CommentRequest commentRequest, User user) {
        Comment reply = commentRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다."));

        if (!reply.getParentComment().getCommentId().equals(parentCommentId)  || !reply.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("유효하지 않은 게시글 또는 댓글 입니다.");
        }

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        reply.updateComment(commentRequest);
        return ResponseDto.success(HttpStatus.CREATED.value());
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, User user) {
        findBoard(boardId);
        User findUser = findUser(user);
        Comment comment = findComment(commentId);

        if (!comment.getUser().equals(findUser)) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }



    // 대댓글 삭제
    @Transactional
    public void deleteReply(Long boardId, Long parentCommentId, Long replyId, User user) {
        Comment reply = commentRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 또는 댓글 입니다."));

        if (!reply.getParentComment().getCommentId().equals(parentCommentId)  || !reply.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("유효하지 않은 게시글 또는 댓글 입니다.");
        }

        if (!reply.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        commentRepository.delete(reply);
    }


    // Comment 객체를 CommentResponse 객체로 변환 후 리스트로 반환
    private List<CommentResponse> convertToDtoList(List<Comment> commentList) {
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponse commentResponse = new CommentResponse(comment);
            addRepliesToResponse(comment, commentResponse);
            commentResponseList.add(commentResponse);
        }
        return commentResponseList;
    }


    private void addRepliesToResponse(Comment comment, CommentResponse commentResponse) {
        for (Comment reply : comment.getReplies()) {
            CommentResponse replyResponse = new CommentResponse(reply);
            commentResponse.addReply(replyResponse);
            addRepliesToResponse(reply, replyResponse);
        }
    }


    // 댓글 찾기
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("없는 댓글 입니다."));
    }


    // 게시글 찾기
    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("없는 게시글입니다."));
    }


    private User findUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
    }
}
