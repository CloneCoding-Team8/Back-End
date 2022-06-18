package com.sparta.cloneproject.model;

import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "PRODUCT_ID_GENERATOR",
        sequenceName = "PRODUCT_SEQUENCES",
        initialValue = 1, allocationSize = 1
)
@Table(name = "PRODUCT")
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "POST_ID_GENERATOR")
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String productimg;

    @Column(nullable = false)
    private int deliveryFee;

    @Column
    private int star;

//    @CreatedDate
//    @Column
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column
//    private LocalDateTime modifiedAt;

    public Product(ProductRequestDto productRequestDto, Map<String , String> imgResult) {
        this.title = productRequestDto.getTitle();
        this.productimg = imgResult.get("url");
        this.deliveryFee = productRequestDto.getDeliveryFee();
        this.price = productRequestDto.getPrice();
    }
}
