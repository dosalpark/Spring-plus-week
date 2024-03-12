package com.example.foodthought.repository.book;

import com.example.foodthought.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long>, BookRepositoryCustom {
    Page<Book> findAllByTitleContains(Pageable pageable, String title);

    List<Book> findAll();

    List<Book> findAllByTitleContains(String title);
}
