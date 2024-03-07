package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.follow.FollowTopResponseDto;
import com.example.foodthought.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowService {

    /**
     * 팔로우, 취소
     * @param user
     * @param followerId
     */
    public void toggleFollow(User user, Long followerId);


    /**
     * 팔로우가 가장많은 회원 3명 출력
     * @return List<FollowTopResponseDto>
     */
    public ResponseDto<List<FollowTopResponseDto>> findFollowerByLikeTop3();

}
