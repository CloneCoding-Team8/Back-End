package com.sparta.cloneproject.requestdto;

import lombok.Getter;

@Getter
public class RefreshTokenRequestDto {
    private String username;
    private String refreshtoken;
}
