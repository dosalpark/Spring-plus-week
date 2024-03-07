package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.dto.comment.CommentResponse;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findAllUser());
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findUser(userId));
    }

    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //board
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteAdminBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteAdminBoard(boardId));
    }


    @PutMapping("/api/boards/{boardId}/status")
    public ResponseEntity<ResponseDto<Boolean>> updateStatusAdminBoard(@PathVariable Long boardId,
                                                                       @RequestBody UpdateStatusRequestDto updateStatusRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateStatusBoard(boardId, updateStatusRequestDto));
    }

    @GetMapping("/api/boards")
    public ResponseEntity<ResponseDto<List<GetBoardAdminResponseDto>>> getAdminAllBoard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sort,
            @RequestParam(defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdminAllBoard(page, size, sort, isAsc));
    }


    //comment
    @PutMapping("/api/boards/{boardId}/comments/{commentId}/block")
    public ResponseEntity blockComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminService.blockComment(boardId, commentId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("/api/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<String> deleteAdminComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminService.deleteAdminComment(boardId, commentId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/api/boards/{boardId}/comments")
    public List<CommentResponse> getAllAdminComment(
            @PathVariable Long boardId) {
        return adminService.getAllComment(boardId);
    }

    //book
    //책 입력
    @PostMapping("/api/books")
    public ResponseEntity<ResponseDto<Boolean>> createBook(
            @RequestPart CreateBookRequestDto createBookRequestDto,
            @RequestPart(value = "bookImage", required = true) MultipartFile file) throws IOException {
        adminService.createBook(createBookRequestDto, file);
        return ResponseEntity.status(HttpStatus.OK).body(adminService.createBook(createBookRequestDto, file));
    }


    //책 수정
    @PutMapping("/api/books/{bookId}")
    public ResponseEntity<ResponseDto<Boolean>> updateBook(
            @PathVariable Long bookId,
            @RequestPart UpdateBookRequestDto updateBookRequestDto,
            @RequestPart(value = "bookImage", required = true) MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateBook(bookId, updateBookRequestDto, file));
    }


    //책 삭제
    @DeleteMapping("/api/books/{bookId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteBook(@PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteBook(bookId));
    }
}