package com.sparta.cloneproject.responsedto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class UserResponseDto {
    private int code = HttpStatus.OK.value();
    private String message;
    private HttpStatus ststus;
}
