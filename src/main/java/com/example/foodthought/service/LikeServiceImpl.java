package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.like.LikeTopResponseDto;
import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.Book;
import com.example.foodthought.entity.Like;
import com.example.foodthought.entity.User;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.example.foodthought.exception.customException.BookNotFoundException;
import com.example.foodthought.repository.book.BookRepository;
import com.example.foodthought.repository.LikeRepository;
import com.example.foodthought.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodthought.exception.ErrorCode.NOT_FOUND_BOARD;
import static com.example.foodthought.exception.ErrorCode.NOT_FOUND_BOOK;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {


    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final BookRepository bookRepository;


    @Override
    @Transactional
    public ResponseDto<Boolean> toggleLike(User user, Long boardId) {
        Board board = findBoard(boardId);
        Like oldLike = findLike(board.getId(), user.getId());
        if (oldLike != null) {
            likeRepository.delete(oldLike);
        } else {
            Like like = toEntity(user, board);
            likeRepository.save(like);
        }
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<LikeTopResponseDto>> findBoardByLikeTop3() {
        List<Object[]> top3 = likeRepository.findBoardByLikeTop3();  //2
        List<LikeTopResponseDto> dtoList = new ArrayList<>();
        for (Object[] objects : top3) {
            Board board = (Board) objects[0];
            LikeTopResponseDto dto = buildLikeTop(board, (Long) objects[1]);
            dtoList.add(dto);
        }
        return ResponseDto.success(200, dtoList);
    }


    private Like findLike(Long boardId, Long userId) {
        return likeRepository.findLikesByBoard_IdAndUser_Id(boardId, userId);
    }


    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new BoardNotFoundException(NOT_FOUND_BOARD));
    }


    private Like toEntity(User user, Board board) {
        return Like.builder()
                .user(user)
                .board(board)
                .build();
    }


    private LikeTopResponseDto buildLikeTop(Board board, Long countLikes) {
        Book book = bookRepository.findById(board.getBookId())
                .orElseThrow(() -> new BookNotFoundException(NOT_FOUND_BOOK));
        return LikeTopResponseDto.builder().boardId(board.getId())
                .booktitle(book.getTitle())
                .bookauthor(book.getAuthor())
                .bookpublisher(book.getPublisher())
                .bookcategory(book.getCategory())
                .bookimage(book.getImage())
                .username(board.getUser().getUsername())
                .contents(board.getContents())
                .countLikes(countLikes).build();
    }
}