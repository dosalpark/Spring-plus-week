package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.auth.LoginUserDto;
import com.example.foodthought.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Boolean>> login(
            @RequestBody LoginUserDto loginUserDto,
            HttpServletResponse response) {
        return ResponseEntity.status(200).body(authService.login(loginUserDto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Boolean>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.status(200).body(authService.logout(request, response));
    }
}
