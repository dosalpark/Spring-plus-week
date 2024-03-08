package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //책 전체조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<GetBookResponseDto>>> getAllBook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sort,
            @RequestParam(defaultValue = "false") boolean isASC,
            @RequestParam(required = false, defaultValue = "") String title //
    ) {
        return ResponseEntity.status(200).body(bookService.getAllBook(page, size, sort, isASC, title));
    }


    //책 단권조회
    @GetMapping("/{bookId}")
    public ResponseEntity<ResponseDto<GetBookResponseDto>> getBook(@PathVariable Long bookId) {
        return ResponseEntity.status(200).body(bookService.getBook(bookId));
    }
}
