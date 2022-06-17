package com.sparta.cloneproject.validator;

import com.sparta.cloneproject.requestdto.UserRequestDto;

public class UserValidator {
    public static void validateUserInput(UserRequestDto singUpData){
        //ID 검사
        if(singUpData.getUsername() == null){
            throw new IllegalArgumentException("아이디를 입력해 주세요");
        }
        if (singUpData.getUsername().equals("")) {
            throw new IllegalArgumentException("아이디를 입력해 주세요");
        }
        if (!singUpData.getUsername().matches("")) {
            throw new IllegalArgumentException("아이디 형식을 확인해 주세요");
        }

        //닉네임 검사
        if(singUpData.getNickname() == null){
            throw new IllegalArgumentException("닉네임을 입력해 주세요");
        }
        if (singUpData.getNickname().equals("")) {
            throw new IllegalArgumentException("닉네임을 입력해 주세요");
        }
        if (!singUpData.getNickname().matches("^[가-힣a-zA-Z]+$")) {
            throw new IllegalArgumentException("닉네임 형식을 확인해 주세요");
        }

        //비밀번호 검사
        if(singUpData.getPassword() == null){
            throw new IllegalArgumentException("비밀번호를 입력해 주세요");
        }
        if (singUpData.getPassword().equals("")) {
            throw new IllegalArgumentException("비밀번호를 입력해 주세요");
        }
        if (!singUpData.getPassword().matches("")) {
            throw new IllegalArgumentException("비밀번호 형식을 확인해 주세요");
        }
    }
}
