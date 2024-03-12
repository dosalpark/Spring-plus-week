package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.GetUsersResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.dto.comment.CommentAdminResponseDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    //user
    @GetMapping("/api/users")
    public ResponseEntity<ResponseDto<List<GetUsersResponseDto>>> getUsers() {
        return ResponseEntity.status(200).body(adminService.findAllUser());
    }


    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(200).body(adminService.deleteUser(userId));
    }


    //board
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteAdminBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(200).body(adminService.deleteAdminBoard(boardId));
    }


    @PutMapping("/api/boards/{boardId}/status")
    public ResponseEntity<ResponseDto<Boolean>> updateStatusAdminBoard(@PathVariable Long boardId,
                                                                       @Valid @RequestBody UpdateStatusRequestDto updateStatusRequestDto) {
        return ResponseEntity.status(200).body(adminService.updateStatusBoard(boardId, updateStatusRequestDto));
    }


    @GetMapping("/api/boards")
    public ResponseEntity<ResponseDto<Page<GetBoardAdminResponseDto>>> getAdminAllBoard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "false") boolean isAsc) {
        return ResponseEntity.status(200).body(adminService.getAdminAllBoard(page, size, sort, isAsc));
    }


    //comment
    @PutMapping("/api/boards/{boardId}/comments/{commentId}/status")
    public ResponseEntity<ResponseDto<Boolean>> updateStatusComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody UpdateStatusRequestDto updateStatusRequestDto) {
        return ResponseEntity.status(200).body(adminService.updateStatusComment(boardId, commentId, updateStatusRequestDto));
    }


    @DeleteMapping("/api/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteAdminComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(200).body(adminService.deleteAdminComment(boardId, commentId));
    }


    @GetMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<ResponseDto<List<CommentAdminResponseDto>>> getAdminComment(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean isAsc) {
        return ResponseEntity.status(200).body(adminService.getAdminComment(boardId, page, size, sort, isAsc));
    }

    //book
    @PostMapping("/api/books")
    public ResponseEntity<ResponseDto<Boolean>> createBook(
            @Valid @RequestPart CreateBookRequestDto createBookRequestDto,
            @RequestPart(value = "bookImage", required = true) MultipartFile file) throws IOException {
        return ResponseEntity.status(201).body(adminService.createBook(createBookRequestDto, file));
    }


    @PutMapping("/api/books/{bookId}")
    public ResponseEntity<ResponseDto<Boolean>> updateBook(
            @PathVariable Long bookId,
            @RequestPart UpdateBookRequestDto updateBookRequestDto,
            @RequestPart(value = "bookImage", required = true) MultipartFile file) throws IOException {
        return ResponseEntity.status(200).body(adminService.updateBook(bookId, updateBookRequestDto, file));
    }


    @DeleteMapping("/api/books/{bookId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteBook(@PathVariable Long bookId) {
        return ResponseEntity.status(200).body(adminService.deleteBook(bookId));
    }
}