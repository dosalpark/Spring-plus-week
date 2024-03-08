package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.like.LikeTopResponseDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class LikeController {


    private final LikeService likeService;


    @PostMapping("/{boardId}/likes")
    public ResponseEntity<ResponseDto<Boolean>> toggleLike(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId) {
        return ResponseEntity.status(200).body(likeService.toggleLike(userDetails.getUser(), boardId));
    }


    //가장 Like 많은 게시물 3개
    @GetMapping("/likes-top3")
    public ResponseEntity<ResponseDto<List<LikeTopResponseDto>>> findBoardByLikeTop3() {
        return ResponseEntity.status(200).body(likeService.findBoardByLikeTop3());
    }
}
