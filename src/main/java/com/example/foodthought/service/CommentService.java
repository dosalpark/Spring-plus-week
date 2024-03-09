package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.comment.CommentAdminResponseDto;
import com.example.foodthought.dto.comment.CommentResponseDto;
import com.example.foodthought.dto.comment.CreateCommentRequestDto;
import com.example.foodthought.dto.comment.UpdateCommentRequest;
import com.example.foodthought.entity.User;

import java.util.List;

public interface CommentService {


    /**
     * 댓글 생성
     *
     * @param boardId                 댓글달 게시글 ID 값
     * @param createCommentRequestDto 댓글 내용
     * @param user                    작성자
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> createParentComment(Long boardId, CreateCommentRequestDto createCommentRequestDto, User user);


    /**
     * 대댓글 생성
     *
     * @param boardId                 댓글달 게시글 ID 값
     * @param parentCommentId         대댓글 달 댓글의 ID 값
     * @param createCommentRequestDto 대댓글 내용
     * @param user                    작성자
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> createChildComment(Long boardId, Long parentCommentId, CreateCommentRequestDto createCommentRequestDto, User user);


    /**
     * 댓글 조회
     *
     * @param boardId 댓글 조회할 게시글 ID 값
     * @param page    default 0
     * @param size    default 3
     * @param sort    정렬기준(String)
     * @param isAsc   ASC, DESC
     * @return List<CommentResponseDto>
     */
    public ResponseDto<List<CommentResponseDto>> getComment(Long boardId, int page, int size, String sort, boolean isAsc);


    /**
     * 댓글 수정
     *
     * @param boardId              댓글 수정할 게시글 ID 값
     * @param commentId            댓글 ID 값
     * @param updateCommentRequest 수정할 댓글 내용
     * @param user                 작성자
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> updateComment(Long boardId, Long commentId, UpdateCommentRequest updateCommentRequest, User user);


//    /**
//     * 대댓글 수정
//     *
//     * @param boardId              대댓글 수정할 게시글 ID 값
//     * @param parentCommentId      대댓글 수정할 댓글 ID 값
//     * @param replyId              수정할 대댓글 ID 값
//     * @param updateCommentRequest 수정할 대댓글 내용
//     * @param user                 작성자
//     * @return 성공시 true, 실패는 Exception 으로 처리
//     */
//    public ResponseDto<Boolean> updateReply(Long boardId, Long parentCommentId, Long replyId, UpdateCommentRequest updateCommentRequest, User user);


    /**
     * 댓글 삭제
     *
     * @param boardId   댓글 삭제할 게시글 ID값
     * @param commentId 삭제할 댓글 ID값
     * @param user      작성자
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> deleteComment(Long boardId, Long commentId, User user);


//    /**
//     * 대댓글 삭제
//     *
//     * @param boardId         대댓글 삭제할 게시글 ID값
//     * @param parentCommentId 대댓글 삭제할 댓글 ID값
//     * @param replyId         삭제할 대댓글 ID 값
//     * @param user            작성자
//     * @return 성공시 true, 실패는 Exception 으로 처리
//     */
//    public ResponseDto<Boolean> deleteReply(Long boardId, Long parentCommentId, Long replyId, User user);


    /**
     * 댓글 상태 변경(관리자)
     *
     * @param boardId                상태변경할 댓글있는 게시글 ID값
     * @param commentId              상태변경할 댓글 ID값
     * @param updateStatusRequestDto 변경할 상태(NOTICE, POST, BLOCKED)
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> updateStatusComment(Long boardId, Long commentId, UpdateStatusRequestDto updateStatusRequestDto);


    /**
     * 댓글 삭제(관리자)
     *
     * @param boardId   삭제할 댓글의 게시글 ID값
     * @param commentId 삭제할 댓글의 ID값
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> deleteAdminComment(Long boardId, Long commentId);


    /**
     * 모든 상태의 댓글 조회(관리자),
     *
     * @param boardId 댓글달린 게시글 ID 값
     * @param page    default 0
     * @param size    default 3
     * @param sort    정렬기준(String)
     * @param isAsc   ASC, DESC
     * @return List<CommentAdminResponseDto>
     */
    public ResponseDto<List<CommentAdminResponseDto>> getAdminComment(Long boardId, int page, int size, String sort, boolean isAsc);
}
