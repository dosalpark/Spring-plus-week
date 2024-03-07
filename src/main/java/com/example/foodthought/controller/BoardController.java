package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;


    //게시물 생성
    @PostMapping
    public ResponseEntity<ResponseDto> createBoard(@RequestBody CreateBoardRequestDto create,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(create, userDetails.getUser()));
    }


    //전체 게시물 조회
    @GetMapping
    public ResponseEntity<ResponseDto> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sort,
            @RequestParam(defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getAllBoards(page, size, sort, isAsc));
    }


    //게시물 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoard(boardId));
    }


    //게시물 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long boardId,
                                            @RequestBody UpdateBoardRequestDto update,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.updateBoard(boardId, update, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
