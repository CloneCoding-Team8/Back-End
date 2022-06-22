//package com.sparta.cloneproject.model;
//
//import com.sparta.cloneproject.requestdto.ProductRequestDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ProductTest {
//
//        @Test
//        @DisplayName("정상 케이스")
//        void createProduct_Normal() {
//        // given
//            Long productId = 100L;
//            String title = "오리온 꼬북칩 초코츄러스맛 160g";
//            String productimg = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
//            int deliveryFee = 1000;
//            int price = 2350;
//
//            ProductRequestDto requestDto = new ProductRequestDto(
//                    title,
//                    price,
//                    deliveryFee
//                    );
//
//        // when
//            Product product = new Product(requestDto, productimg, productId);
//
//        // then
//            //assertNull(product.getProductId());
//            assertEquals(productId, product.getId());
//            assertEquals(title, product.getTitle());
//            assertEquals(productimg, product.getProductimg());
//            assertEquals(price, product.getPrice());
//            assertEquals(deliveryFee, product.getDeliveryFee());
//            assertEquals(0, product.getStar());
//    }
//}
