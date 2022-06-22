package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.requestdto.RefreshTokenRequestDto;
import com.sparta.cloneproject.requestdto.UserRequestDto;
import com.sparta.cloneproject.responsedto.JwtResponseDto;
import com.sparta.cloneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody UserRequestDto singUpData) {
        return userService.signupUser(singUpData);
    }

    //회원가입 아이디 중복 확인
    @PostMapping("/signup/idcheck")
    public boolean signupUseridCheck(@RequestBody UserRequestDto signupUseridCheckData) {
        return userService.signupUseridCheck(signupUseridCheckData);
    }

    //로그인
    @PostMapping("/login")
    public JwtResponseDto loginUser(@RequestBody UserRequestDto loginData) {
        return userService.loginUser(loginData);
    }

    //Access 토큰 재발급
    @PostMapping("/newaccesstoken")
    public JwtResponseDto newAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return userService.newAccessToken(refreshTokenRequestDto);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {

        System.out.println(refreshTokenRequestDto.getRefreshtoken());
        System.out.println(refreshTokenRequestDto.getUsername());

        return userService.logout(refreshTokenRequestDto);
    }
}
