package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.comment.CommentAdminResponseDto;
import com.example.foodthought.dto.comment.CommentResponseDto;
import com.example.foodthought.dto.comment.CreateCommentRequestDto;
import com.example.foodthought.dto.comment.UpdateCommentRequest;
import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Comment;
import com.example.foodthought.entity.Status;
import com.example.foodthought.entity.User;
import com.example.foodthought.exception.customException.*;
import com.example.foodthought.repository.BoardRepository;
import com.example.foodthought.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.foodthought.entity.Status.BLOCKED;
import static com.example.foodthought.entity.Status.NOTICE;
import static com.example.foodthought.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {


    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    @Override
    @Transactional
    public ResponseDto<Boolean> createParentComment(Long boardId, CreateCommentRequestDto createCommentRequestDto, User user) {
        Board board = findBoard(boardId);
        commentRepository.save(toParentEntity(board, createCommentRequestDto.getContents(), user));
        return ResponseDto.success(201, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> createChildComment(Long boardId, Long parentCommentId, CreateCommentRequestDto createCommentRequestDto, User user) {
        Board board = findBoard(boardId);
        Comment parentComment = findComment(parentCommentId);
        findBoardByParentComment(board.getId(), parentComment.getBoard().getId());
        commentRepository.save(toChildEntity(board, createCommentRequestDto.getContents(), parentComment, user));
        return ResponseDto.success(201, true);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<CommentResponseDto>> getComment(Long boardId, int page, int size, String sort, boolean isAsc) {
        findBoard(boardId);
        findAllComment(boardId);
        PageRequest pageRequest = PageRequest.of(page, size, isAsc ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Comment> commentList = commentRepository.findByBoardIdAndParentCommentIsNullAndStatusNotIn(boardId, Arrays.asList(Status.NOTICE, Status.BLOCKED), pageRequest);
        return ResponseDto.success(200, convertToDtoList(commentList));
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateComment(Long boardId, Long commentId, UpdateCommentRequest updateCommentRequest, User user) {
        Board board = findBoard(boardId);
        Comment comment = findComment(commentId);
        findBoardByComment(board.getId(), comment.getBoard().getId());
        checkOwnerAndStatus(comment, user);
        comment.updateComment(updateCommentRequest);
        return ResponseDto.success(200, true);
    }


//    @Override
//    @Transactional
//    public ResponseDto<Boolean> updateReply(Long boardId, Long parentCommentId, Long replyId, UpdateCommentRequest updateCommentRequest, User user) {
//        Board board = findBoard(boardId);
//        Comment parentComment = findComment(parentCommentId);
//        findBoardByParentComment(board.getId(),parentComment.getBoard().getId());
//        Comment reply = findComment(replyId);
//        checkOwnerAndStatus(reply, user);
//        reply.updateComment(updateCommentRequest);
//        return ResponseDto.success(200, true);
//    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteComment(Long boardId, Long commentId, User user) {
        Board board = findBoard(boardId);
        Comment comment = findComment(commentId);
        findBoardByComment(board.getId(), comment.getBoard().getId());
        checkOwnerAndStatus(comment, user);
        commentRepository.deleteAll(deleteRelatedChildComment(commentId));
        commentRepository.delete(comment);
        return ResponseDto.success(200, true);
    }


//    @Override
//    @Transactional
//    public ResponseDto<Boolean> deleteReply(Long boardId, Long parentCommentId, Long replyId, User user) {
//        Board board = findBoard(boardId);
//        Comment parentComment = findComment(parentCommentId);
//        findBoardByParentComment(board.getId(),parentComment.getBoard().getId());
//        Comment reply = findComment(replyId);
//        checkOwnerAndStatus(reply, user);
//        commentRepository.delete(reply);
//        return ResponseDto.success(200, true);
//
//    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateStatusComment(Long boardId, Long commentId, UpdateStatusRequestDto updateStatusRequestDto) {
        findBoard(boardId);
        Comment comment = findComment(commentId);
        comment.updateStatusComment(updateStatusRequestDto);
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteAdminComment(Long boardId, Long commentId) {
        findBoard(boardId);
        Comment comment = findComment(commentId);
        commentRepository.deleteAll(deleteRelatedChildComment(commentId));
        commentRepository.delete(comment);
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<CommentAdminResponseDto>> getAdminComment(Long boardId, int page, int size, String sort, boolean isAsc) {
        findBoard(boardId);
        findAllAdminComment(boardId);
        PageRequest pageRequest = PageRequest.of(page, size, isAsc ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Comment> commentList = commentRepository.findByBoardIdAndParentCommentIsNull(boardId, pageRequest);
        return ResponseDto.success(200, adminConvertToDtoList(commentList));
    }


    private Comment toParentEntity(Board board, String contents, User user) {
        return Comment.builder()
                .contents(contents)
                .board(board)
                .user(user)
                .build();
    }


    private Comment toChildEntity(Board board, String contents, Comment parentComment, User user) {
        return Comment.builder()
                .contents(contents)
                .board(board)
                .user(user)
                .parentComment(parentComment)
                .build();
    }


    private void checkOwnerAndStatus(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId()) ||
                comment.getStatus().equals(BLOCKED) ||
                comment.getStatus().equals(NOTICE)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }


    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException(NOT_FOUND_COMMENT));
    }


    private void findBoardByComment(Long boardId, Long commentBoardId) {
        if (!boardId.equals(commentBoardId)) {
            throw new CommentMismatchException(COMMENT_MISMATCH_BOARD);
        }
    }
//    private Comment findParentComment(Long parentCommentId) {
//        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(
//                () -> new CommentNotFoundException(NOT_FOUND_COMMENT));
//        if (!Objects.isNull(parentComment.getParentComment())) {
//            throw new CommentReplyNotAllowedException(COMMENT_REPLY_NOT_ALLOWED);
//        }
//        return parentComment;
//    }


    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD));
    }


    private void findBoardByParentComment(Long boardId, Long parentCommentBoardId) {
        if (!boardId.equals(parentCommentBoardId)) {
            throw new BoardReplyNotAllowedException(REPLY_NOT_ALLOWED);
        }
    }


    private List<Comment> deleteRelatedChildComment(Long parentCommentId) {
        return commentRepository.findCommentsByParentComment_CommentId(parentCommentId);
    }


    private void findAllComment(Long boardId) {
        if (commentRepository.findByBoardIdAndStatusNotIn(boardId, Arrays.asList(Status.NOTICE, Status.BLOCKED)).isEmpty()) {
            throw new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD);
        }
    }


    private void findAllAdminComment(Long boardId) {
        if (commentRepository.findByBoardId(boardId).isEmpty()) {
            throw new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD);
        }
    }


    private List<CommentResponseDto> convertToDtoList(Page<Comment> commentList) {
        List<CommentResponseDto> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponseDto dto = CommentResponseDto.builder()
                    .contents(comment.getContents())
                    .userId(comment.getUser().getUserId())
                    .createAt(comment.getCreateAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build();
            addRepliesToResponse(comment, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }


    private void addRepliesToResponse(Comment comment, CommentResponseDto commentResponseDto) {
        for (Comment reply : comment.getReplies()) {
            CommentResponseDto replyResponse = CommentResponseDto.builder()
                    .contents(reply.getContents())
                    .userId(reply.getUser().getUserId())
                    .createAt(reply.getCreateAt())
                    .modifiedAt(reply.getModifiedAt())
                    .build();
            commentResponseDto.addReply(replyResponse);

        }
    }


    private List<CommentAdminResponseDto> adminConvertToDtoList(Page<Comment> commentList) {
        List<CommentAdminResponseDto> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentAdminResponseDto dto = CommentAdminResponseDto.builder()
                    .contents(comment.getContents())
                    .userId(comment.getUser().getUserId())
                    .createAt(comment.getCreateAt())
                    .modifiedAt(comment.getModifiedAt())
                    .status(comment.getStatus())
                    .build();
            addAdminRepliesToResponse(comment, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }


    private void addAdminRepliesToResponse(Comment comment, CommentAdminResponseDto commentAdminResponseDto) {
        for (Comment reply : comment.getReplies()) {
            CommentAdminResponseDto dto = CommentAdminResponseDto.builder()
                    .contents(reply.getContents())
                    .userId(reply.getUser().getUserId())
                    .createAt(reply.getCreateAt())
                    .modifiedAt(reply.getModifiedAt())
                    .status(reply.getStatus())
                    .build();
            commentAdminResponseDto.addReply(dto);
        }
    }
}
