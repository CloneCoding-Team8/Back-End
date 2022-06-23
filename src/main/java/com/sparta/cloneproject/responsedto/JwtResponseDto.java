package com.sparta.cloneproject.responsedto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class JwtResponseDto {
    private int code = HttpStatus.OK.value();
    private String accesstoken;
    private String refreshtoken;

    private String message;
    private HttpStatus errorststus;
}
