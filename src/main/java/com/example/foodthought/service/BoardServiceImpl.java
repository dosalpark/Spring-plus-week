package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.*;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.example.foodthought.exception.customException.BookNotFoundException;
import com.example.foodthought.exception.customException.PermissionDeniedException;
import com.example.foodthought.repository.BookRepository;
import com.example.foodthought.repository.CommentRepository;
import com.example.foodthought.repository.LikeRepository;
import com.example.foodthought.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.foodthought.entity.Status.BLOCKED;
import static com.example.foodthought.entity.Status.NOTICE;
import static com.example.foodthought.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {


    private final BoardRepository boardRepository;
    private final BookRepository bookRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public ResponseDto<Boolean> createBoard(CreateBoardRequestDto create, User user) {
        Book book = findBook(create.getBookId());
        boardRepository.save(toEntity(create, user));
        return ResponseDto.success(201, true);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<Page<GetBoardResponseDto>> getAllBoards(int page, int size, String sort, boolean isAsc) {
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<GetBoardResponseDto> boards = boardRepository.findAllByStatusIn(pageRequest);
        return ResponseDto.success(200, boards);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetBoardResponseDto> getBoard(Long boardId) {
        Board board = findBoard(boardId);
        return ResponseDto.success(200, convertToDto(board));
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, User user) {
        Board board = findBoard(boardId);
        findBook(updateBoardRequestDto.getBookId());
        checkOwnerAndStatus(board, user);
        board.update(updateBoardRequestDto, user);
        boardRepository.save(board);
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteBoard(Long boardId, User user) {
        Board board = findBoard(boardId);
        checkOwnerAndStatus(board, user);
        likeRepository.deleteAll(deleteRelatedLike(boardId));
        commentRepository.deleteAll(deleteRelatedComment(boardId));
        boardRepository.delete(board);
        return ResponseDto.success(200, true);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<Page<GetBoardAdminResponseDto>> getAdminAllBoard(int page, int size, String sort, boolean isAsc) {
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<GetBoardAdminResponseDto> boards = boardRepository.findAllAdmin(pageRequest);
        return ResponseDto.success(200, boards);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteAdminBoard(Long boardId) {
        Board board = findBoard(boardId);
        likeRepository.deleteAll(deleteRelatedLike(boardId));
        commentRepository.deleteAll(deleteRelatedComment(boardId));
        boardRepository.delete(board);
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateStatusBoard(Long boardId, UpdateStatusRequestDto updateStatusRequestDto) {
        Board board = findBoard(boardId);
        board.updateStatusBoard(updateStatusRequestDto);
        return ResponseDto.success(200, true);
    }


    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD));
    }


    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException(NOT_FOUND_BOOK));
    }


    private void checkOwnerAndStatus(Board board, User user) {
        if (!board.getUser().getId().equals(user.getId()) ||
                board.getStatus().equals(BLOCKED) ||
                board.getStatus().equals(NOTICE)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }


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


    private Board toEntity(CreateBoardRequestDto dto, User user) {
        return Board.builder()
                .bookId(dto.getBookId())
                .user(user)
                .contents(dto.getContents()).build();
    }

    private List<Like> deleteRelatedLike(Long boardId) {
        return likeRepository.findLikesByBoard_Id(boardId);
    }


    private List<Comment> deleteRelatedComment(Long boardId) {
        return commentRepository.findCommentsByBoard_Id(boardId);
    }
}