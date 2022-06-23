package com.sparta.cloneproject.model;

import com.sparta.cloneproject.requestdto.ProductRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PRODUCT")
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column
    private String commaPrice;

    @Column(nullable = false)
    private String productimg;

    @Column(nullable = false)
    private int deliveryFee;

    @Column
    private String commaDeliveryFee;

    @Column
    private double star;

    @Column
    private int reviewCount = 0;


    public Product(ProductRequestDto productRequestDto, Map<String , String> imgResult) {
        this.title = productRequestDto.getTitle();
        this.productimg = imgResult.get("url");
        this.deliveryFee = productRequestDto.getDeliveryFee();
        this.price = productRequestDto.getPrice();
        DecimalFormat df = new DecimalFormat("###,###");
        this.commaPrice = df.format(productRequestDto.getPrice());
        this.commaDeliveryFee = df.format(productRequestDto.getDeliveryFee());
    }
//Integer.toString(this.deliveryFee).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
    public void upreviewcount(){
        reviewCount++;
    }

    public void downreviewcount(){
        reviewCount--;
    }
}
