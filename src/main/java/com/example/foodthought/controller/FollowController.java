package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FollowController {
    private final FollowService followService;


    //팔로잉/취소
    @PostMapping("/{userId}/follows")
    public ResponseEntity<ResponseDto> toggleFollow(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(followService.toggleFollow(userDetails.getUser(), userId));
    }

    //가장 follow 많은 회원 3명
    @GetMapping("/follows-top3")
    public ResponseEntity<ResponseDto> findFollowerByLikeTop3() {
        return ResponseEntity.status(201).body(followService.findFollowerByLikeTop3());
    }
}

