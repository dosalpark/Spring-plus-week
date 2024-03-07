package com.example.foodthought.repository;

import com.example.foodthought.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findByBoardId(Long boardId);
    List<Comment> findByBoardIdAndParentCommentIsNull(Long boardId);

    List<Comment> findCommentsByBoard_Id(Long boardId);
}
