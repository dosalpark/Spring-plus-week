package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.follow.FollowTopResponseDto;
import com.example.foodthought.entity.Follow;
import com.example.foodthought.entity.User;
import com.example.foodthought.repository.FollowRepository;
import com.example.foodthought.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {


    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ResponseDto<Boolean> toggleFollow(User user, Long followerId) {
        User follower = findUser(user.getId());
        Follow oldFollow = findFollow(user.getId(), follower.getId());
        if (oldFollow != null) {
            followRepository.delete(oldFollow);
        } else {
            Follow follow = toEntity(user, follower);
            followRepository.save(follow);
        }
        boolean success = true;
        return ResponseDto.success(200, success);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<FollowTopResponseDto>> findFollowerByLikeTop3() {
        List<Object[]> top3 = followRepository.findFollowerByLikeTop3();
        List<FollowTopResponseDto> dtoList = new ArrayList<>();
        for (Object[] objects : top3) {
            User user = (User) objects[0];
            FollowTopResponseDto dto = buildFollowTop(user, (Long) objects[1]);
            dtoList.add(dto);
        }
        return ResponseDto.success(200, dtoList);
    }


    private Follow findFollow(Long followingId, Long followerId) {
        if (followingId.equals(followerId)) {
            throw new IllegalArgumentException("본인을 Follow 할 수 없습니다.");
        }
        return followRepository.findFollowsByFollowing_IdAndFollower_Id(followingId, followerId);
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당하는  유저가 없습니다."));
    }


    private Follow toEntity(User following, User follower) {
        return Follow.builder()
                .following(following)
                .follower(follower)
                .build();
    }


    private FollowTopResponseDto buildFollowTop(User user, Long contFollows) {
        return FollowTopResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .intro(user.getIntro())
                .userPhoto(user.getUserPhoto())
                .countFollows(contFollows).build();
    }
}
