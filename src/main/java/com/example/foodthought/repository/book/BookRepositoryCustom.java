package com.example.foodthought.repository.book;

import com.example.foodthought.dto.book.GetBookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {

    Page<GetBookResponseDto> findAllByTitleContains(Pageable pageable, String title);
}
