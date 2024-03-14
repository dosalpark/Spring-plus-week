package com.example.foodthought.test;

import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.*;


public interface DummyTest extends CommonTest {
    Long TEST_BOOK_ID = 1L;
    Long TEST_ANOTHER_BOOK_ID = 2L;
    Long TEST_BOARD_ID = 1L;
    Long TEST_ANOTHER_BOARD_ID = 2L;
    Long TEST_BLOCKED_BOARD_ID = 3L;
    Long TEST_LIKE_ID = 1L;
    Long TEST_PARENT_COMMENT_ID = 1L;
    Long TEST_CHILD_COMMENT_ID = 2L;
    String TEST_BOARD_CONTENT = "content";
    String TEST_UPDATE_BOARD_CONTENT = "update content";
    String TEST_UPDATE_STATUS_POST_REQUEST_DTO = "POST";
    String TEST_UPDATE_STATUS_BLOCKED_REQUEST_DTO = "BLOCKED";
    String TEST_RESPONSE_TITLE = "title";
    String TEST_RESPONSE_AUTHOR = "author";
    String TEST_RESPONSE_PUBLISHER = "publisher";
    String TEST_RESPONSE_IMAGE = "url";
    String TEST_RESPONSE_CATEGORY = "category";
    String TEST_RESPONSE_CONTENT = "content";
    Status TEST_RESPONSE_STATUS_POST = Status.POST;
    Status TEST_RESPONSE_STATUS_BLOCKED = Status.BLOCKED;

    String TEST_COMMENT_CONTENTS = "commentContent";


    CreateBoardRequestDto TEST_BOARD_REQUEST_DTO = CreateBoardRequestDto.builder()
            .bookId(TEST_BOOK_ID)
            .contents(TEST_BOARD_CONTENT)
            .build();

    CreateBoardRequestDto TEST_ANOTHER_BOARD_REQUEST_DTO = CreateBoardRequestDto.builder()
            .bookId(TEST_ANOTHER_BOOK_ID)
            .contents(TEST_BOARD_CONTENT)
            .build();

    UpdateBoardRequestDto TEST_UPDATE_BOARD_REQUEST_DTO = UpdateBoardRequestDto.builder()
            .bookId(TEST_ANOTHER_BOOK_ID)
            .contents(TEST_UPDATE_BOARD_CONTENT)
            .build();

    UpdateBoardRequestDto TEST_ANOTHER_UPDATE_BOARD_REQUEST_DTO = UpdateBoardRequestDto.builder()
            .bookId(TEST_ANOTHER_BOOK_ID)
            .contents(TEST_BOARD_CONTENT)
            .build();

    GetBoardResponseDto TEST_BOARD_RESPONSE_DTO = GetBoardResponseDto.builder()
            .title(TEST_RESPONSE_TITLE)
            .author(TEST_RESPONSE_AUTHOR)
            .publisher(TEST_RESPONSE_PUBLISHER)
            .image(TEST_RESPONSE_IMAGE)
            .category(TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(TEST_RESPONSE_CONTENT)
            .build();

    GetBoardResponseDto TEST_ANOTHER_BOARD_RESPONSE_DTO = GetBoardResponseDto.builder()
            .title(TEST_RESPONSE_TITLE)
            .author(TEST_RESPONSE_AUTHOR)
            .publisher(TEST_RESPONSE_PUBLISHER)
            .image(TEST_RESPONSE_IMAGE)
            .category(TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(TEST_RESPONSE_CONTENT)
            .build();

    GetBoardResponseDto TEST_BLOCKED_BOARD_RESPONSE_DTO = GetBoardResponseDto.builder()
            .title(BLOCKED_PREFIX + TEST_RESPONSE_TITLE)
            .author(BLOCKED_PREFIX + TEST_RESPONSE_AUTHOR)
            .publisher(BLOCKED_PREFIX + TEST_RESPONSE_PUBLISHER)
            .image(BLOCKED_PREFIX + TEST_RESPONSE_IMAGE)
            .category(BLOCKED_PREFIX + TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(BLOCKED_PREFIX + TEST_RESPONSE_CONTENT)
            .build();


    GetBoardResponseDto TEST_ANOTHER_USER_BOARD_RESPONSE_DTO = GetBoardResponseDto.builder()
            .title(ANOTHER_PREFIX + TEST_RESPONSE_TITLE)
            .author(ANOTHER_PREFIX + TEST_RESPONSE_AUTHOR)
            .publisher(ANOTHER_PREFIX + TEST_RESPONSE_PUBLISHER)
            .image(ANOTHER_PREFIX + TEST_RESPONSE_IMAGE)
            .category(ANOTHER_PREFIX + TEST_RESPONSE_CATEGORY)
            .userId(ANOTHER_PREFIX + TEST_USER_USERID)
            .contents(ANOTHER_PREFIX + TEST_RESPONSE_CONTENT)
            .build();


    GetBoardAdminResponseDto TEST_ADMIN_BOARD_RESPONSE_DTO = GetBoardAdminResponseDto.builder()
            .title(TEST_RESPONSE_TITLE)
            .author(TEST_RESPONSE_AUTHOR)
            .publisher(TEST_RESPONSE_PUBLISHER)
            .image(TEST_RESPONSE_IMAGE)
            .category(TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(TEST_RESPONSE_CONTENT)
            .status(TEST_RESPONSE_STATUS_POST)
            .build();

    GetBoardAdminResponseDto TEST_ADMIN_ANOTHER_BOARD_RESPONSE_DTO = GetBoardAdminResponseDto.builder()
            .title(TEST_RESPONSE_TITLE)
            .author(TEST_RESPONSE_AUTHOR)
            .publisher(TEST_RESPONSE_PUBLISHER)
            .image(TEST_RESPONSE_IMAGE)
            .category(TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(TEST_RESPONSE_CONTENT)
            .status(TEST_RESPONSE_STATUS_POST)
            .build();

    GetBoardAdminResponseDto TEST_ADMIN_BLOCKED_BOARD_RESPONSE_DTO = GetBoardAdminResponseDto.builder()
            .title(BLOCKED_PREFIX + TEST_RESPONSE_TITLE)
            .author(BLOCKED_PREFIX + TEST_RESPONSE_AUTHOR)
            .publisher(BLOCKED_PREFIX + TEST_RESPONSE_PUBLISHER)
            .image(BLOCKED_PREFIX + TEST_RESPONSE_IMAGE)
            .category(BLOCKED_PREFIX + TEST_RESPONSE_CATEGORY)
            .userId(TEST_USER_USERID)
            .contents(BLOCKED_PREFIX + TEST_RESPONSE_CONTENT)
            .status(TEST_RESPONSE_STATUS_BLOCKED)
            .build();

    GetBoardAdminResponseDto TEST_ADMIN_ANOTHER_USER_BOARD_RESPONSE_DTO = GetBoardAdminResponseDto.builder()
            .title(ANOTHER_PREFIX + TEST_RESPONSE_TITLE)
            .author(ANOTHER_PREFIX + TEST_RESPONSE_AUTHOR)
            .publisher(ANOTHER_PREFIX + TEST_RESPONSE_PUBLISHER)
            .image(ANOTHER_PREFIX + TEST_RESPONSE_IMAGE)
            .category(ANOTHER_PREFIX + TEST_RESPONSE_CATEGORY)
            .userId(ANOTHER_PREFIX + TEST_USER_USERID)
            .contents(ANOTHER_PREFIX + TEST_RESPONSE_CONTENT)
            .status(TEST_RESPONSE_STATUS_POST)
            .build();


    UpdateStatusRequestDto TEST_STATUS_BLOCKED_UPDATE_REQUEST_DTO = UpdateStatusRequestDto.builder()
            .status(TEST_UPDATE_STATUS_BLOCKED_REQUEST_DTO)
            .build();

    Book TEST_BOOK = Book.builder()
            .title(TEST_RESPONSE_TITLE)
            .author(TEST_RESPONSE_AUTHOR)
            .publisher(TEST_RESPONSE_PUBLISHER)
            .image(TEST_RESPONSE_IMAGE)
            .category(TEST_RESPONSE_CATEGORY)
            .build();

    Book TEST_ANOTHER_BOOK = Book.builder()
            .title(ANOTHER_PREFIX + TEST_RESPONSE_TITLE)
            .author(ANOTHER_PREFIX + TEST_RESPONSE_AUTHOR)
            .publisher(ANOTHER_PREFIX + TEST_RESPONSE_PUBLISHER)
            .image(ANOTHER_PREFIX + TEST_RESPONSE_IMAGE)
            .category(ANOTHER_PREFIX + TEST_RESPONSE_CATEGORY)
            .build();

    Board TEST_BOARD = Board.builder()
            .user(TEST_USER)
            .bookId(TEST_BOOK_ID)
            .status(Status.POST)
            .contents(TEST_BOARD_CONTENT)
            .build();

    Board TEST_ANOTHER_BOARD = Board.builder()
            .user(TEST_USER)
            .contents(TEST_BOARD_CONTENT)
            .status(Status.POST)
            .bookId(TEST_BOOK_ID)
            .build();
    Board TEST_BLOCK_BOARD = Board.builder()
            .user(TEST_USER)
            .bookId(TEST_BOOK_ID)
            .status(Status.BLOCKED)
            .contents(TEST_BOARD_CONTENT)
            .build();

    Board TEST_ANOTHER_USER_BOARD = Board.builder()
            .user(TEST_ANOTHER_USER)
            .contents(TEST_BOARD_CONTENT)
            .status(Status.POST)
            .bookId(TEST_ANOTHER_BOOK_ID)
            .build();

    Like TEST_LIKE = Like.builder()
            .id(TEST_LIKE_ID)
            .board(TEST_BOARD)
            .user(TEST_USER)
            .build();

    Comment TEST_PARENT_COMMENT = Comment.builder()
            .commentId(TEST_PARENT_COMMENT_ID)
            .user(TEST_USER)
            .board(TEST_BOARD)
            .build();

//    Comment TEST_CHILD_COMMENT = Comment.builder()
//            .commentId(TEST_CHILD_COMMENT_ID)
//            .user(TEST_ANOTHER_USER)
//            .board(TEST_BOARD)
//            .parentComment(TEST_PARENT_COMMENT)
//            .build();
//    List<Comment> childComments = java.util.List.of(TEST_CHILD_COMMENT);
}
