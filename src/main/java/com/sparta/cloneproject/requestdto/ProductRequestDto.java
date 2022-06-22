package com.sparta.cloneproject.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequestDto {
    private String title;
    private int price;
    private int deliveryFee;
    private String productimg;

    public ProductRequestDto(String title, int price, int deliveryFee) {
        this.title = title;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }
}
