package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.*;
import com.example.foodthought.repository.BoardRepository;
import com.example.foodthought.repository.BookRepository;
import com.example.foodthought.repository.CommentRepository;
import com.example.foodthought.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BookRepository bookRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    //게시물 생성
    @Transactional
    public ResponseDto<Void> createBoard(CreateBoardRequestDto create, User user) {
        Book book = findBook(create.getBookId());
        boardRepository.save(convertToBoard(create, user));
        return ResponseDto.success(201);
    }


    //게시물 모두조회
    public ResponseDto<List<GetBoardResponseDto>> getAllBoards(int page, int size, String sort, boolean isAsc) {
        if (boardRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("등록된 게시물이 없습니다.");
        }
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<Board> boards = boardRepository.findAllByStatusNot(Status.Blocked, pageRequest);
        return ResponseDto.success(200, convertToDtoList(boards));
    }


    //게시물 단건조회
    public ResponseDto<GetBoardResponseDto> getBoard(Long boardId) {
        Board board = findBoard(boardId);
        return ResponseDto.success(200, convertToDto(board));
    }


    //게시물 수정
    @Transactional
    public void updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, User user) {
        Board board = findBoard(boardId);
        if (!isMyBoard(board, user)) {
            throw new IllegalArgumentException("게시물을 수정 할 수 있는 권한이 없습니다.");
        }
        board.update(updateBoardRequestDto, user);
        boardRepository.save(board);
    }


    //게시물 삭제
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        if (!isMyBoard(board, user)) {
            throw new IllegalArgumentException("게시물을 삭제할 수 있는 권한이 없습니다.");
        }
        List<Like> deleteLike = likeRepository.findLikesByBoard_Id(boardId);
        likeRepository.deleteAll(deleteLike);
        List<Comment> deleteComment = commentRepository.findCommentsByBoard_Id(boardId);
        commentRepository.deleteAll(deleteComment);
        boardRepository.delete(board);
    }

    @Transactional
    public void deleteAdminBoard(Long boardId) {
        Board board = findBoard(boardId);
        List<Like> deleteLike = likeRepository.findLikesByBoard_Id(boardId);
        likeRepository.deleteAll(deleteLike);
        List<Comment> deleteComment = commentRepository.findCommentsByBoard_Id(boardId);
        commentRepository.deleteAll(deleteComment);
        boardRepository.delete(board);
    }

    @Transactional
    public void blockBoard(Long boardId) {
        Board board = findBoard(boardId);
        board.block();
    }


    public ResponseDto<List<GetBoardAdminResponseDto>> getAdminAllBoard(int page, int size, String sort, boolean isAsc) {
        if (boardRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("등록된 게시물이 없습니다.");
        }
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        return ResponseDto.success(200, adminConvertToDtoList(boards));
    }


    //게시물 찾기
    private Board findBoard(Long boardId) {

        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
    }


    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다."));
    }


    //본인게시물인지 확인
    private boolean isMyBoard(Board board, User user) {
        return board.getUser().getId().equals(user.getId());
    }


    //Board -> GetBoardResponseDto
    private GetBoardResponseDto convertToDto(Board board) {
        Book book = findBook(board.getBookId());
        return GetBoardResponseDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .image(book.getImage())
                .category(book.getCategory())
                .contents(board.getContents()).build();
    }


    //boards -> GetBoardResponseDtos
    private List<GetBoardResponseDto> convertToDtoList(Page<Board> boards) {
        List<GetBoardResponseDto> dtoList = new ArrayList<>();
        for (Board board : boards) {
            Book book = findBook(board.getBookId());
            GetBoardResponseDto dto = GetBoardResponseDto.builder()
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .image(book.getImage())
                    .category(book.getCategory())
                    .contents(board.getContents()).build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    private List<GetBoardAdminResponseDto> adminConvertToDtoList(Page<Board> boards) {
        List<GetBoardAdminResponseDto> dtoList = new ArrayList<>();
        for (Board board : boards) {
            Book book = findBook(board.getBookId());
            GetBoardAdminResponseDto dto = GetBoardAdminResponseDto.builder()
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .image(book.getImage())
                    .category(book.getCategory())
                    .contents(board.getContents())
                    .Status(String.valueOf(board.getStatus()))
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }


    //book+user+dto -> board
    private Board convertToBoard(CreateBoardRequestDto dto, User user) {
        return Board.builder()
                .bookId(dto.getBookId())
                .user(user)
                .contents(dto.getContents()).build();
    }
}