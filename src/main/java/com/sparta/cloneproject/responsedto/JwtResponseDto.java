package com.sparta.cloneproject.responsedto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class JwtResponseDto {
    private int code = HttpStatus.OK.value();
    private Object result;

    public JwtResponseDto() {}

    public JwtResponseDto(Object result) {
        this.result = result;
    }

    public JwtResponseDto(int code, Object result) {
        this.code = code;
        this.result = result;
    }

    protected void setResult (Object result){
        this.result = result;
    }
}
