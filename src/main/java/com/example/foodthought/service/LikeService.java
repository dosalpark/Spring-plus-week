package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.like.LikeTopResponseDto;
import com.example.foodthought.entity.User;

import java.util.List;


public interface LikeService {

    /**
     * 좋아요 생성, 취소
     * @param user
     * @param boardId
     */
    public void toggleLike(User user, Long boardId);


    /**
     * 좋아요가 가장많은 3개의 게시물 조회
     * @return List<LikeTopResponseDto>
     */
    public ResponseDto<List<LikeTopResponseDto>> findBoardByLikeTop3();
}