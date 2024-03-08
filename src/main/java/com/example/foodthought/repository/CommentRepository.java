package com.example.foodthought.repository;

import com.example.foodthought.entity.Comment;
import com.example.foodthought.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);
    Page<Comment> findByBoardIdAndParentCommentIsNull(Long boardId, Pageable pageable);
    List<Comment> findByBoardIdAndStatusNotIn(Long boardId, List<Status> statuses);
    Page<Comment> findByBoardIdAndParentCommentIsNullAndStatusNotIn(Long boardId, List<Status> statuses, Pageable pageable);

    List<Comment> findCommentsByBoard_Id(Long boardId);

    List<Comment> findCommentsByParentComment_CommentId(Long parentCommentId);
}
