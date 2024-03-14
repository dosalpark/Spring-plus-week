package com.example.foodthought.board;

import com.example.foodthought.config.QueryFactoryConfig;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.entity.Board;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.example.foodthought.repository.UserRepository;
import com.example.foodthought.repository.board.BoardRepository;
import com.example.foodthought.repository.board.BoardRepositoryImpl;
import com.example.foodthought.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.foodthought.exception.ErrorCode.NOT_FOUND_SEARCH_BOARD;
import static com.example.foodthought.test.CommonTest.TEST_ANOTHER_USER;
import static com.example.foodthought.test.CommonTest.TEST_USER;
import static com.example.foodthought.test.DummyTest.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryFactoryConfig.class)
@ActiveProfiles("test")
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardRepositoryImpl boardRepositoryImpl;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;


    public void setup() {
        userRepository.save(TEST_USER);
        userRepository.save(TEST_ANOTHER_USER);
        bookRepository.save(TEST_BOOK);
        bookRepository.save(TEST_ANOTHER_BOOK);
        boardRepository.save(TEST_BOARD);
        boardRepository.save(TEST_ANOTHER_BOARD);
        boardRepository.save(TEST_BLOCK_BOARD);
        boardRepository.save(TEST_ANOTHER_USER_BOARD);
    }

    PageRequest getPageable() {
        int page = 0;
        int size = 4;
        String sort = "createdAt";
        boolean isAsc = false;
        return PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
    }

    @Test
    @DisplayName("유저가 작성한 모든글 조회 - 성공")
    void test1() {
        //given
        setup();

        //when
        List<Board> boardList = boardRepository.findBoardsByUser_Id(TEST_USER_ID);

        //then
        assertEquals(3, boardList.size());
        assertEquals(TEST_USER_ID, boardList.stream().findAny().get().getUser().getId());
    }

    @Test
    @DisplayName("모든 게시물 조회 - 성공")
    void test2() {
        //given
        setup();
        PageRequest pageable = getPageable();

        //when
        Page<GetBoardResponseDto> getAllBoard = boardRepositoryImpl.findAllBoard(pageable);

        //then
        assertNotNull(getAllBoard);
        assertEquals(3, getAllBoard.getTotalElements());
    }

    @Test
    @DisplayName("모든 게시물 조회 - 게시물이 존재하지 않을 때")
    void test3() {
        //given
        PageRequest pageable = getPageable();

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardRepositoryImpl.findAllBoard(pageable));

        //then
        assertEquals(NOT_FOUND_SEARCH_BOARD.getMessage(), exception.getErrorCode().getMessage());
        assertEquals(NOT_FOUND_SEARCH_BOARD.getStatus(), exception.getErrorCode().getStatus());

    }

    @Test
    @DisplayName("[관리자]모든 게시물 조회 - 성공")
    void test4() {
        //given
        setup();
        PageRequest pageable = getPageable();

        //when
        Page<GetBoardAdminResponseDto> getAllAdminBoard = boardRepositoryImpl.findAllAdmin(pageable);

        //then
        assertNotNull(getAllAdminBoard);
        assertEquals(4, getAllAdminBoard.getTotalElements());
    }

    @Test
    @DisplayName("[관리자]모든 게시물 조회 - 게시물이 존재하지 않을 때")
    void test5() {
        //given
        PageRequest pageable = getPageable();

        //when
        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class, () ->
                boardRepositoryImpl.findAllAdmin(pageable));

        //then
        assertEquals(NOT_FOUND_SEARCH_BOARD.getMessage(), exception.getErrorCode().getMessage());
        assertEquals(NOT_FOUND_SEARCH_BOARD.getStatus(), exception.getErrorCode().getStatus());

    }

}