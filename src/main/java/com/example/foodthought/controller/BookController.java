package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //책 전체조회
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GetBookResponseDto>>> getAllBook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false, defaultValue = "") String title //
    ) {
        return ResponseEntity.status(200).body(bookService.getAllBook(page, size, sort, isAsc, title));
    }


    //책 단권조회
    @GetMapping("/{bookId}")
    public ResponseEntity<ResponseDto<GetBookResponseDto>> getBook(@PathVariable Long bookId) {
        return ResponseEntity.status(200).body(bookService.getBook(bookId));
    }
}
