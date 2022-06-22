package com.sparta.cloneproject.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequestDto {
    private String username;
    private String nickname;
    private String password;
}
