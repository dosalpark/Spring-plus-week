package com.example.foodthought.repository;

import com.example.foodthought.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findLikesByBoard_IdAndUser_Id(Long boardId, Long userId);


    @Query(value = "SELECT l.board, COUNT(l) FROM Like l GROUP BY l.board ORDER BY COUNT(l) DESC LIMIT 3")
    List<Object[]> findBoardByLikeTop3();

    List<Like> findLikesByBoard_Id(Long boardId);

    List<Like> findLikesByUser_Id(Long userId);
}
