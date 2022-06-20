package com.sparta.cloneproject.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketRequestDto {
    private Long productid;
//    private Long userid;
    private int itemcount;
}
