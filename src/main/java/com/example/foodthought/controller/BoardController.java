package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ResponseDto<Boolean>> createBoard(@Valid @RequestBody CreateBoardRequestDto create,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(201).body(boardService.createBoard(create, userDetails.getUser()));
    }


    //전체 게시물 조회
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GetBoardResponseDto>>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(200).body(boardService.getAllBoards(page, size, sort, isAsc));
    }


    //게시물 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<GetBoardResponseDto>> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.status(200).body(boardService.getBoard(boardId));
    }


    //게시물 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Boolean>> updateBoard(@PathVariable Long boardId,
                                                            @RequestBody UpdateBoardRequestDto update,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(boardService.updateBoard(boardId, update, userDetails.getUser()));
    }


    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Boolean>> deleteBoard(@PathVariable Long boardId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(200).body(boardService.deleteBoard(boardId, userDetails.getUser()));
    }
}
