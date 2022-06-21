package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.repository.ReviewRepository;
import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import com.sparta.cloneproject.responsedto.ReviewResponseDto;
import com.sparta.cloneproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {

//    private final ProductRepository productRepository;
    private final ReviewService reviewService;

    // 상품 id 로 Review 조회
    @GetMapping("/api/review/list/{productid}")
    public List<ReviewResponseDto> getReview(@PathVariable Long productid) {
//        productrepository.findByProductid(productid).orElseThrow(
//                ()->new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        return reviewService.getReview(productid);
    }

    // Review 작성
    @PostMapping("/api/review")
    public String createReview(@RequestBody ReviewRequestDto requestDto) {
        reviewService.createReview(requestDto);
        return "후기 작성 완료";
    }

    // Review 수정
    @PutMapping("/api/review/{reviewid}")
    public String updateReview(@PathVariable Long reviewid, @RequestBody ReviewRequestDto requestDto, String username) {
        String updateResult = reviewService.update(reviewid, requestDto, username);
        return updateResult;
    }

    // Review 삭제
    @DeleteMapping("/api/review/{reviewid}")
    public String deleteReview(@PathVariable Long reviewid, String username) {
        String deleteResult = reviewService.deleteReview(reviewid, username);
        return deleteResult;
    }
}