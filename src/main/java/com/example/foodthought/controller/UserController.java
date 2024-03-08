package com.example.foodthought.controller;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.GetUsersResponseDto;
import com.example.foodthought.dto.user.CreateUserDto;
import com.example.foodthought.dto.user.UpdateUserDto;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @PostMapping(value = "/signup",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto<Boolean>> signUp(
            @RequestPart @Valid CreateUserDto createUserDto,
            @RequestPart(value = "userPhoto",required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.status(201).body(userService.createUser(createUserDto,file));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<GetUsersResponseDto>> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(200).body(userService.findOneUser(userId));
    }


    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto<Boolean>> updateUser(@PathVariable Long userId,
                                     @RequestPart @Valid UpdateUserDto updateUserDto,
                                     @RequestPart(value = "userPhoto",required = false) MultipartFile file,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseEntity.status(200).body(userService.updateUser(userId,updateUserDto,file,userDetails.getUser()));
    }
}
