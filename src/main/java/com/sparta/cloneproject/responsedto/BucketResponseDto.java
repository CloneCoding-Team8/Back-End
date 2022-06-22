package com.sparta.cloneproject.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BucketResponseDto {
    private Long id;
    private String productImg;
    private String title;
    private int itemCount;
    private int price;
    private String commaPrice;
    private int deliveryFee;
    private String commaDeliveryFee;
}
