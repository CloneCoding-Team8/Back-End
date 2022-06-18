package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.repository.UserRepository;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final AwsS3Service s3Service;

    //게시글 작성
    //로그인한 유저 정보를 받아와서
    //새로등록한 게시글의 happyPoint만큼 +해주고 DB에 업데이트


    public void createProduct(ProductRequestDto productRequestDto, MultipartFile multipartFile) {

        Map<String, String> imgResult = s3Service.uploadFile(multipartFile);

        //새 상품 등록(백엔드용)
        Product product = new Product(productRequestDto, imgResult);
        productRepository.save(product);
    }

    //상품 상세페이지
    public Optional<Product> getProductDetails(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        return product;
    }

    //전체상품 리스팅
    public List<Product> getAllProductList() {
        List<Product> productList = productRepository.findAllByOrderByCreatedAtDesc();

        return productList;
    }
}
