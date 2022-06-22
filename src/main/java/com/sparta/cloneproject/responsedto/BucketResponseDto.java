package com.sparta.cloneproject.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketResponseDto {
    private Long id;
    private String productimg;
    private String title;
    private int itemcount;
    private int price;
    private int delivery;

}
