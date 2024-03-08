package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.auth.LoginUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {


    /**
     * 로그인
     *
     * @param loginUserDto 로그인에 필요한 정보
     * @param response     이용자에게 JwtToken 전달해줌
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> login(LoginUserDto loginUserDto, HttpServletResponse response);


    /**
     * 로그아웃
     *
     * @param request  로그인한 이용자의 쿠키보유
     * @param response 초기화된 쿠키를 전달해줌
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> logout(HttpServletRequest request, HttpServletResponse response);
}
