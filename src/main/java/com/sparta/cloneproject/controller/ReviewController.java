package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.model.Review;
import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import com.sparta.cloneproject.security.UserDetailsImpl;
import com.sparta.cloneproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    // 상품 id 로 Review 조회
    @GetMapping("/api/review/list/{productid}")
    public Page<Review> getReview(@PathVariable Long productid,
                                  @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return reviewService.getReview(productid, pageable);
    }

    // Review 작성
    @PostMapping("/api/review/{productid}")
    public String createReview(@PathVariable Long productid,
                               @RequestPart(value = "itemimg", required = false) MultipartFile itemimg,
                               @RequestPart(value = "reviewdata") ReviewRequestDto requestDto,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        String nickname = userDetails.getNickname();
        reviewService.createReview(productid, itemimg, requestDto, nickname, username);
        return "후기 작성 완료";
    }

    // Review 수정
    @PatchMapping("/api/review/{reviewid}")
    public String updateReview(@PathVariable Long reviewid,
                               @RequestBody ReviewRequestDto requestDto,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return reviewService.update(reviewid, requestDto, username);
    }

    // Review 삭제
    @DeleteMapping("/api/review/{reviewid}")
    public String deleteReview(@PathVariable Long reviewid,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return reviewService.deleteReview(reviewid, username);
    }
}