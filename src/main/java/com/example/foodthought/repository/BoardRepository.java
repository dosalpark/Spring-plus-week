package com.example.foodthought.repository;

import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long>, PagingAndSortingRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAllByStatusNot(Status status, Pageable pageable);
    List<Board> findAll();

}

