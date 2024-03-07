package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.GetUsersResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.dto.comment.CommentAdminResponseDto;
import com.example.foodthought.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final BookService bookService;
    private final PasswordEncoder passwordEncoder;

    //user
    public List<GetUsersResponseDto> findAllUser() {
        return userService.findAllUser()
                .stream()
                .map(user -> GetUsersResponseDto.builder()
                        .id(user.getId())
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .intro(user.getIntro())
                        .userPhoto(user.getUserPhoto())
                        .build())
                .toList();
    }

    public GetUsersResponseDto findUser(Long userId) {
        User user = userService.findUser(userId);
        return GetUsersResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .intro(user.getIntro())
                .userPhoto(user.getUserPhoto())
                .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }


    //board
    @Transactional
    public ResponseDto<Boolean> deleteAdminBoard(Long boardId) {
        return boardService.deleteAdminBoard(boardId);
    }


    @Transactional
    public ResponseDto<Boolean> updateStatusBoard(Long boardId, UpdateStatusRequestDto updateStatusRequestDto) {
        return boardService.updateStatusBoard(boardId, updateStatusRequestDto);
    }


    public ResponseDto<List<GetBoardAdminResponseDto>> getAdminAllBoard(int page, int size, String sort, boolean isAsc) {
        return boardService.getAdminAllBoard(page, size, sort, isAsc);
    }


    //comment
    @Transactional
    public ResponseDto<Boolean> deleteAdminComment(Long boardId, Long commentId) {
        return commentService.deleteAdminComment(boardId, commentId);
    }


    @Transactional
    public ResponseDto<Boolean> updateStatusComment(Long boardId, Long commentId, UpdateStatusRequestDto updateStatusRequestDto) {
        return commentService.updateStatusComment(boardId, commentId, updateStatusRequestDto);
    }


    public ResponseDto<List<CommentAdminResponseDto>> getAdminComment(Long boardId, int page, int size, String sort, boolean isAsc) {
        return commentService.getAdminComment(boardId, page, size, sort, isAsc);
    }


    // book
    public ResponseDto<Boolean> createBook(CreateBookRequestDto createBookRequestDto, MultipartFile file) throws IOException {
        return bookService.createBook(createBookRequestDto, file);
    }


    public ResponseDto<Boolean> updateBook(Long bookId, UpdateBookRequestDto updateBookRequestDto, MultipartFile file) throws IOException {
        return bookService.updateBook(bookId, updateBookRequestDto, file);
    }


    public ResponseDto<Boolean> deleteBook(Long bookId) {
        return bookService.deleteBook(bookId);
    }

}