package com.sparta.cloneproject.model;

import com.sparta.cloneproject.requestdto.ProductRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PRODUCT")
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String productimg;

    @Column(nullable = false)
    private int deliveryFee;

    @Column
    private double star;

    @Column
    private int reviewCount;


    public Product(ProductRequestDto productRequestDto, Map<String , String> imgResult) {
        this.title = productRequestDto.getTitle();
        this.productimg = imgResult.get("url");
        this.deliveryFee = productRequestDto.getDeliveryFee();
        this.price = productRequestDto.getPrice();
    }

    public Product(ProductRequestDto requestDto, String productimg, Long productId) {
        this.id = productId;
        this.title = requestDto.getTitle();
        this.deliveryFee = requestDto.getDeliveryFee();
        this.price = requestDto.getPrice();
        this.productimg = productimg;
    }
}
