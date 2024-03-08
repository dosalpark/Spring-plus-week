package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.GetUsersResponseDto;
import com.example.foodthought.dto.user.CreateUserDto;
import com.example.foodthought.dto.user.UpdateUserDto;
import com.example.foodthought.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {


    /**
     * 이용자 한명 조회
     *
     * @param userId 조회할 이용자 ID값
     * @return GetUsersResponseDto
     */
    public ResponseDto<GetUsersResponseDto> findOneUser(Long userId);


    /**
     * 이용자 등록
     *
     * @param createUserDto 등록할 이용자의 정보
     * @param file          등록할 이용자의 사진
     * @return 성공시 true 반환, 실패시 exception
     * @throws IOException 파일이 없거나 비정상적인 경우 발생
     */
    public ResponseDto<Boolean> createUser(CreateUserDto createUserDto, MultipartFile file) throws IOException;

    /**
     * 이용자 수정
     *
     * @param userId        수정할 이용자의 ID값
     * @param updateUserDto 수정할 정보
     * @param file          수정할 이용자의 사진
     * @param user          로그인한 유저 정보
     * @return 성공시 true 반환, 실패시 exception
     * @throws IOException 파일이 없거나 비정상적인 경우 발생
     */
    public ResponseDto<Boolean> updateUser(Long userId, UpdateUserDto updateUserDto, MultipartFile file, User user) throws IOException;

    /**
     * 모든 이용자 검색
     *
     * @return List<GetUsersResponseDto>
     */
    public ResponseDto<List<GetUsersResponseDto>> findAllUser();

    /**
     * 이용자 삭제
     *
     * @param userId 삭제할 이용자의 ID 값
     * @return 성공시 true 반환, 실패시 exception
     */
    public ResponseDto<Boolean> deleteUser(Long userId);
}
