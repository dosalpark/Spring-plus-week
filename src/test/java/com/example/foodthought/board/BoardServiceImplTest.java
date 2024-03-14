package com.example.foodthought.board;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Comment;
import com.example.foodthought.entity.Like;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.example.foodthought.exception.customException.BookNotFoundException;
import com.example.foodthought.exception.customException.PermissionDeniedException;
import com.example.foodthought.repository.CommentRepository;
import com.example.foodthought.repository.LikeRepository;
import com.example.foodthought.repository.board.BoardRepository;
import com.example.foodthought.repository.book.BookRepository;
import com.example.foodthought.service.BoardServiceImpl;
import com.example.foodthought.test.DummyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.foodthought.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class BoardServiceImplTest implements DummyTest {
    @Mock
    BoardRepository boardRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    LikeRepository likeRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    BoardServiceImpl boardService;

    @Test
    @DisplayName("게시물 작성 - 성공")
    void test1() {
        //given
        given(bookRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_BOOK));
        given(boardRepository.save(any(Board.class))).willReturn(TEST_BOARD);

        //when
        ResponseDto<Boolean> result = boardService.createBoard(TEST_BOARD_REQUEST_DTO, TEST_USER);

        //then
        assertEquals(result.getData(), true);
        assertEquals(result.getStatus(), 201);
    }

    @Test
    @DisplayName("게시물 작성 - 실패")
    void test2() {
        //given
        given(bookRepository.findById(any(Long.class))).willThrow(new BookNotFoundException(NOT_FOUND_BOOK));

        //when
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, ()
                -> {
            boardService.createBoard(TEST_BOARD_REQUEST_DTO, TEST_USER);
        });

        //then
        assertEquals(NOT_FOUND_BOOK.getMessage(), exception.getErrorCode().getMessage());
        assertEquals(NOT_FOUND_BOOK.getStatus(), exception.getErrorCode().getStatus());
    }

    @Test
    @DisplayName("게시물 전체 조회 - 성공")
    void test3() {
        //given
        List<GetBoardResponseDto> boardList = new ArrayList<>();
        boardList.add(TEST_BOARD_RESPONSE_DTO);
        boardList.add(TEST_ANOTHER_BOARD_RESPONSE_DTO);
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        boolean isAsc = false;
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<GetBoardResponseDto> boardPage = new PageImpl<>(boardList);
        given(boardRepository.findAllBoard(pageRequest)).willReturn(boardPage);

        //when
        ResponseDto<Page<GetBoardResponseDto>> response = boardService.getAllBoards(page, size, sort, isAsc);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(boardPage, response.getData());
    }

    @Test
    @DisplayName("게시물 단건 조회 - 성공")
    void test4() {
        //given
        given(boardRepository.findById(any())).willReturn(Optional.of(TEST_BOARD));
        given(bookRepository.findById(any())).willReturn(Optional.of(TEST_BOOK));

        //when
        ResponseDto<GetBoardResponseDto> response = boardService.getBoard(TEST_BOARD_ID);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(TEST_RESPONSE_AUTHOR, response.getData().getAuthor());
    }

    @Test
    @DisplayName("게시물 단건 조회 - 책정보가 없을 때")
    void test5() {
        //given
        given(boardRepository.findById(any())).willReturn(Optional.of(TEST_BOARD));
        given(bookRepository.findById(any())).willThrow(new BookNotFoundException(NOT_FOUND_BOOK));

        //when
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, ()
                -> {
            boardService.getBoard(TEST_BOARD_ID);
        });

        //then
        assertEquals(NOT_FOUND_BOOK.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOOK.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 단건 조회 - 게시물이 없을 때")
    void test6() {
        //given
        given(boardRepository.findById(any())).willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, ()
                -> {
            boardService.getBoard(TEST_BOARD_ID);
        });

        //then
        assertEquals(NOT_FOUND_BOARD.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOARD.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 수정 - 성공")
    void test7() {
        //given
        UpdateBoardRequestDto requestDto = TEST_UPDATE_BOARD_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOARD));
        given(bookRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOOK));
        //when
        ResponseDto<Boolean> response = boardService.updateBoard(TEST_BOARD_ID, requestDto, TEST_USER);
        //then
        assertEquals(200, response.getStatus());
        assertEquals(true, response.getData());
    }

    @Test
    @DisplayName("게시물 수정 - 게시물이 없을때")
    void test8() {
        //given
        UpdateBoardRequestDto requestDto = TEST_UPDATE_BOARD_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));
        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardService.updateBoard(TEST_BOARD_ID, requestDto, TEST_USER)
        );

        //then
        assertEquals(NOT_FOUND_BOARD.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOARD.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 수정 - 수정하는 값에 해당하는 책이 없을 때")
    void test9() {
        //given
        UpdateBoardRequestDto requestDto = TEST_UPDATE_BOARD_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BLOCK_BOARD));
        given(bookRepository.findById(any(Long.class))).willThrow(new BookNotFoundException(NOT_FOUND_BOOK));
        //when
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () ->
                boardService.updateBoard(TEST_BOARD_ID, requestDto, TEST_USER)
        );

        //then
        assertEquals(NOT_FOUND_BOOK.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOOK.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 수정 - 작성자와 로그인한 유저정보가 다를때")
    void test10() {
        //given
        UpdateBoardRequestDto requestDto = TEST_UPDATE_BOARD_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOARD));
        given(bookRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOOK));
        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
                boardService.updateBoard(TEST_BOARD_ID, requestDto, TEST_ANOTHER_USER)
        );

        //then
        assertEquals(PERMISSION_DENIED.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(PERMISSION_DENIED.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 수정 - 게시물이 BLOCKED 상태일때")
    void test11() {
        //given
        UpdateBoardRequestDto requestDto = TEST_UPDATE_BOARD_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BLOCK_BOARD));
        given(bookRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOOK));
        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
                boardService.updateBoard(TEST_BOARD_ID, requestDto, TEST_USER)
        );

        //then
        assertEquals(PERMISSION_DENIED.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(PERMISSION_DENIED.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 삭제 - 성공")
    void test12() {
        //given
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOARD));
        List<Like> likes = new ArrayList<>();
        likes.add(TEST_LIKE);
        given(likeRepository.findLikesByBoard_Id(any(Long.class))).willReturn(likes);
        List<Comment> comments = new ArrayList<>();
        comments.add(TEST_PARENT_COMMENT);
        given(commentRepository.findCommentsByBoard_Id(any(Long.class))).willReturn(comments);

        //when
        ResponseDto<Boolean> response = boardService.deleteBoard(TEST_BOARD_ID, TEST_USER);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(true, response.getData());
    }

    @Test
    @DisplayName("게시물 삭제 - 게시물이 없을 때")
    void test13() {
        //given
        given(boardRepository.findById(any(Long.class))).willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardService.deleteBoard(TEST_BOARD_ID, TEST_USER)
        );

        //then
        assertEquals(NOT_FOUND_BOARD.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOARD.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 삭제 - 작성자와 로그인한 유저정보가 다를때")
    void test14() {
        //given
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BOARD));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
                boardService.deleteBoard(TEST_BOARD_ID, TEST_ANOTHER_USER)
        );

        //then
        assertEquals(PERMISSION_DENIED.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(PERMISSION_DENIED.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("게시물 삭제 - 게시물이 BLOCKED 상태일 때")
    void test15() {
        //given
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_BLOCK_BOARD));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
                boardService.deleteBoard(TEST_BOARD_ID, TEST_ANOTHER_USER));

        //then
        assertEquals(PERMISSION_DENIED.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(PERMISSION_DENIED.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("[관리자]게시물 전체 조회 - 성공")
    void test16() {
        //given
        List<GetBoardAdminResponseDto> boardList = new ArrayList<>();
        boardList.add(TEST_ADMIN_BOARD_RESPONSE_DTO);
        boardList.add(TEST_ADMIN_ANOTHER_BOARD_RESPONSE_DTO);
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        boolean isAsc = false;
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<GetBoardAdminResponseDto> boardPage = new PageImpl<>(boardList);
        given(boardRepository.findAllAdmin(pageRequest)).willReturn(boardPage);

        //when
        ResponseDto<Page<GetBoardAdminResponseDto>> response = boardService.getAdminAllBoard(page, size, sort, isAsc);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(boardPage, response.getData());
    }

    @Test
    @DisplayName("[관리자]게시물 삭제 - 성공(타인게시물삭제)")
    void test17() {
        //given
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_ANOTHER_USER_BOARD));
        List<Like> likes = new ArrayList<>();
        likes.add(TEST_LIKE);
        given(likeRepository.findLikesByBoard_Id(any(Long.class))).willReturn(likes);
        List<Comment> comments = new ArrayList<>();
        comments.add(TEST_PARENT_COMMENT);
        given(commentRepository.findCommentsByBoard_Id(any(Long.class))).willReturn(comments);

        //when
        ResponseDto<Boolean> response = boardService.deleteAdminBoard(TEST_BOARD_ID);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(true, response.getData());
    }

    @Test
    @DisplayName("[관리자]게시물 삭제 - 게시물이 없을 때")
    void test18() {
        //given
        given(boardRepository.findById(any(Long.class))).willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardService.deleteAdminBoard(TEST_BOARD_ID)
        );

        //then
        assertEquals(NOT_FOUND_BOARD.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOARD.getMessage(), exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("[관리자]게시물 상태변경 - 성공")
    void test19() {
        //given
        UpdateStatusRequestDto requestDto = TEST_STATUS_BLOCKED_UPDATE_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willReturn(Optional.of(TEST_ANOTHER_USER_BOARD));

        //when
        ResponseDto<Boolean> response = boardService.updateStatusBoard(TEST_BOARD_ID, requestDto);

        //then
        assertEquals(200, response.getStatus());
        assertEquals(true, response.getData());
    }

    @Test
    @DisplayName("[관리자]게시물 상태변경 - 게시물이 없을 때")
    void test20() {
        //given
        UpdateStatusRequestDto requestDto = TEST_STATUS_BLOCKED_UPDATE_REQUEST_DTO;
        given(boardRepository.findById(any(Long.class))).willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardService.updateStatusBoard(TEST_BOARD_ID, requestDto)
        );

        //then
        assertEquals(NOT_FOUND_BOARD.getStatus(), exception.getErrorCode().getStatus());
        assertEquals(NOT_FOUND_BOARD.getMessage(), exception.getErrorCode().getMessage());
    }

}