package com.example.foodthought.repository.board;

import com.example.foodthought.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findBoardsByUser_Id(Long userId);
}

