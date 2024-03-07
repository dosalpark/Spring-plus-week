package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.follow.FollowTopResponseDto;
import com.example.foodthought.entity.User;

import java.util.List;

public interface FollowService {

    /**
     * 팔로우, 취소
     *
     * @param user       로그인한 유저
     * @param followerId 팔로우할 유저 ID
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> toggleFollow(User user, Long followerId);


    /**
     * 팔로우가 가장많은 회원 3명 출력
     *
     * @return List<FollowTopResponseDto>
     */
    public ResponseDto<List<FollowTopResponseDto>> findFollowerByLikeTop3();
}
