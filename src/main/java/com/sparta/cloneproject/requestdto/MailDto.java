package com.sparta.cloneproject.requestdto;

import com.sparta.cloneproject.responsedto.BucketResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {
    private String address;
    private String title;
    private String content;
    //private List<BucketResponseDto> content;
    //상품, 수량, 금액, 배송비, 합계금액


}