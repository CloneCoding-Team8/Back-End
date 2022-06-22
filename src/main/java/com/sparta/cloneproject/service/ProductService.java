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

    private final ReviewRepository reviewRepository;
    private final AwsS3Service s3Service;

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

    //전체상품 리스팅
    public Page<Product> getAllProductList(Pageable pageble) {

        return productRepository.findAllByOrderByCreatedAtDesc(pageble);
    }

    public void avgStarR(Long reviewid) {
//        List<Review> reviews = reviewRepository.findByProductid(productid);
        Optional<Review> a = reviewRepository.findById(reviewid);
        Long productid1 = a.get().getProductid();

        Product product = productRepository.findById(productid1).orElseThrow(null);
        List<Review> list = reviewRepository.findAllByProductid(productid1);

        int sumStar = 0;
        double avgStar = 0;

        for (Review review : list) {
           sumStar += review.getStar();
        }

        avgStar = Math.round((sumStar/product.getReviewCount()) *100)/100.0;
        product.setStar(avgStar);
        productRepository.save(product);
    }

    public void avgStarP(Long productid) {
//        List<Review> reviews = reviewRepository.findByProductid(productid);
        Product product = productRepository.findById(productid).orElseThrow(null);
        List<Review> list = reviewRepository.findAllByProductid(productid);

        int sumStar = 0;
        double avgStar = 0;

        for (Review review : list) {
            sumStar += review.getStar();
        }

        avgStar = sumStar/product.getReviewCount();
        product.setStar(avgStar);
        productRepository.save(product);
    }

    public void reveiwCountP(Long productid) {
        int reveiwCount = reviewRepository.findAllByProductid(productid).size();
        Product product = productRepository.findById(productid).orElseThrow(null);
        product.setReviewCount(reveiwCount);
        productRepository.save(product);
    }

    public void reveiwCountR(Long reviewid) {
        Optional<Review> a = reviewRepository.findById(reviewid);
        Long productid1 = a.get().getProductid();

        int reveiwCount = reviewRepository.findAllByProductid(productid1).size();
        Product product = productRepository.findById(productid1).orElseThrow(null);
        product.setReviewCount(reveiwCount);
        productRepository.save(product);
    }
}
