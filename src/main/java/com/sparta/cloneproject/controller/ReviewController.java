package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.model.Review;

import com.sparta.cloneproject.repository.ReviewRepository;

import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import com.sparta.cloneproject.security.UserDetailsImpl;
import com.sparta.cloneproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    // 상품 id 로 Review 조회
    @GetMapping("/api/review/list/{productid}")
    public Page<Review> getReview(@PathVariable Long productid,
                                  @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return reviewService.getReview(productid, pageable);
    }

    // reviewid로 Review 조회
    @GetMapping("/api/review/detail/{reviewid}")
    public Review getThatReview(@PathVariable Long reviewid) {
        return reviewRepository.findById(reviewid).orElseThrow(
                ()->new IllegalArgumentException("해당 후기가 존재하지 않습니다."));
    }

    // Review 작성
    @PostMapping("/api/review/{productid}")
    public ResponseEntity<?> createReview(@PathVariable Long productid,
                                          @RequestPart(value = "itemimg", required = false) MultipartFile itemimg,
                                          @RequestPart(value = "reviewdata") ReviewRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        String nickname = userDetails.getNickname();

        return reviewService.createReview(productid, itemimg, requestDto, nickname, username);
    }

    // Review 수정
    @PatchMapping("/api/review/{reviewid}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewid,
                               @RequestBody ReviewRequestDto requestDto,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        return reviewService.updateReview(reviewid, requestDto, username);
    }

    // Review 삭제
    @DeleteMapping("/api/review/{reviewid}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewid,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        return reviewService.deleteReview(reviewid, username);

    }
}