package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.GetUsersResponseDto;
import com.example.foodthought.dto.user.CreateUserDto;
import com.example.foodthought.dto.user.UpdateUserDto;
import com.example.foodthought.entity.*;
import com.example.foodthought.exception.customException.ImageNotFoundException;
import com.example.foodthought.exception.customException.PermissionDeniedException;
import com.example.foodthought.exception.customException.UserNotFoundException;
import com.example.foodthought.repository.CommentRepository;
import com.example.foodthought.repository.FollowRepository;
import com.example.foodthought.repository.LikeRepository;
import com.example.foodthought.repository.UserRepository;
import com.example.foodthought.repository.board.BoardRepository;
import com.example.foodthought.util.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.foodthought.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetUsersResponseDto> findOneUser(Long userId) {
        User user = findUser(userId);
        GetUsersResponseDto dto = GetUsersResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .intro(user.getIntro())
                .userPhoto(user.getUserPhoto())
                .build();
        return ResponseDto.success(200, dto);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> createUser(CreateUserDto createUserDto, MultipartFile file) throws IOException {
        Optional<User> sameUser = userRepository.findByUserId(createUserDto.getUserId());

        if (sameUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }

        String passwordEncryption = passwordEncoder.encode(createUserDto.getPassword());
        User user = new User(createUserDto, passwordEncryption, convertToString(file));
        userRepository.save(user);

        return ResponseDto.success(201, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateUser(Long userId, UpdateUserDto updateUserDto, MultipartFile file, User user) throws IOException {
        User updateUser = findUser(userId);
        verifyIdentity(updateUser.getId(), user.getId());
        updateUser.update(updateUserDto, convertToString(file));
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<GetUsersResponseDto>> findAllUser() {
        return ResponseDto.success(200, convertToDtoList());
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteUser(Long userId) {
        followRepository.deleteAll(followRepository.findFollowsByUserId(userId));
        likeRepository.deleteAll(likeRepository.findLikesByUser_Id(userId));
        commentRepository.deleteAll(commentRepository.findCommentsByUser_Id(userId));
        boardRepository.deleteAll(boardRepository.findBoardsByUser_Id(userId));
        userRepository.delete(findUser(userId));
        return ResponseDto.success(200, true);
    }


    private List<GetUsersResponseDto> convertToDtoList() {
        return userRepository.findAll()
                .stream()
                .map(user -> GetUsersResponseDto.builder()
                        .id(user.getId())
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .intro(user.getIntro())
                        .userPhoto(user.getUserPhoto())
                        .build())
                .toList();
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_USER));
    }


    private void verifyIdentity(Long updateUserId, Long loginUserId) {
        if (!updateUserId.equals(loginUserId)) {
            throw new PermissionDeniedException(PERMISSION_DENIED);
        }
    }


    private String convertToString(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ImageNotFoundException(NOT_FOUND_IMAGE);
        }
        return storageService.uploadFile(file);
    }


    private List<Board> deleteRelatedBoard(Long userId) {
        return boardRepository.findBoardsByUser_Id(userId);
    }


    private List<Comment> deleteRelatedComment(Long userId) {
        return commentRepository.findCommentsByUser_Id(userId);
    }


    private List<Like> deleteRelatedLike(Long userId) {
        return likeRepository.findLikesByUser_Id(userId);
    }


    private List<Follow> deleteRelatedFollow(Long userId) {
        return followRepository.findFollowsByUserId(userId);
    }
}
