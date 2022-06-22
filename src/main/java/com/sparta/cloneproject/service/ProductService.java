package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.model.Review;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.repository.ReviewRepository;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AwsS3Service s3Service;
    private final ReviewRepository reviewRepository;

    //새 상품 등록(백엔드용)
    public void createProduct(ProductRequestDto productRequestDto, MultipartFile multipartFile) {
        Map<String, String> imgResult = s3Service.uploadFile(multipartFile);
        Product product = new Product(productRequestDto, imgResult);
        productRepository.save(product);
    }

    //상품 상세페이지
    public Optional<Product> getProductDetails(Long productId) {
        return productRepository.findById(productId);
    }

    //전체상품 리스팅 int page, int size, String sortBy, boolean isAsc
    public Page<Product> getAllProductList(Pageable pageble) {
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAllByOrderByCreatedAtDesc(pageble);
    }
}
