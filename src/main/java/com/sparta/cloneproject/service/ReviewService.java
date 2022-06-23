package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.model.Review;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.repository.ReviewRepository;
import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AwsS3Service s3Service;


    // Review 조회
    public Page<Review> getReview(Long productid, Pageable pageable) {
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//        Pageable pageable = PageRequest.of(page, size, sort);
        return reviewRepository.findAllByProductidOrderByCreatedAtDesc(productid, pageable);
    }

    // Review 작성
    @Transactional
    public ResponseEntity<?> createReview(Long productid, MultipartFile itemimg,
                                       ReviewRequestDto requestDto,
                                       String nickname,
                                       String username) {
        Map<String, String> reviewimg = s3Service.uploadFile(itemimg);
        Review review = new Review(productid, requestDto, reviewimg, nickname, username);
        reviewRepository.save(review);

        Product product = productRepository.findById(productid).orElse(null);
        product.upreviewcount();
        product.setStar(product.getStar() + (double)requestDto.getStar());
        return new ResponseEntity<>("리뷰 작성이 성공했습니다", HttpStatus.CREATED);
    }

    // Review 수정
    @Transactional
    public ResponseEntity<?> updateReview(Long reviewid, ReviewRequestDto requestDto, String username) {
        Review review = reviewRepository.findById(reviewid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않습니다."));

        String writerId = review.getUsername();
        Long productid = review.getProductid();

        if (Objects.equals(writerId, username)) {
            Product product = productRepository.findById(productid).orElse(null);
            product.setStar(product.getStar() - (double)review.getStar() + requestDto.getStar());

            review.reviewUpdate(requestDto);

            return new ResponseEntity<>("리뷰 수정이 성공했습니다", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("리뷰 작성자가 아닙니다", HttpStatus.FORBIDDEN);
    }

    // Review 삭제
    public ResponseEntity<?> deleteReview(Long reviewid, String username) {
        String writerId = reviewRepository.findById(reviewid).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")).getUsername();

        Review review = reviewRepository.findById(reviewid).orElse(null);
        Long productid = review.getProductid();

        if (Objects.equals(writerId, username)) {
            Product product = productRepository.findById(productid).orElse(null);
            product.downreviewcount();
            product.setStar(product.getStar() - (double)review.getStar());

            reviewRepository.deleteById(reviewid);

            return new ResponseEntity<>("리뷰 삭제가 성공했습니다", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("리뷰 작성자가 아닙니다", HttpStatus.FORBIDDEN);
    }
}