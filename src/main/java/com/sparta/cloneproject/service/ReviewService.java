package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Review;
import com.sparta.cloneproject.repository.ReviewRepository;
import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import com.sparta.cloneproject.responsedto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Review 조회
    public List<ReviewResponseDto> getReview(Long productid) {
        List<Review> reviews = reviewRepository.findAllByProductidOrderByCreatedAtDesc(productid);
        List<ReviewResponseDto> listReviews = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDto reviewsResponseDto = ReviewResponseDto.builder()
                    .content(review)
                    .build();
            listReviews.add(reviewsResponseDto);
        }
        return listReviews;
    }

    // Review 작성
    @Transactional
    public Review createReview(ReviewRequestDto requestDto, Long productid, String nickname, String username) {
//        String reviewCheck = requestDto.getContent();
//        if (reviewCheck.contains("script") || reviewCheck.contains("<") || reviewCheck.contains(">")) {
//            Review review = new Review(requestDto, "xss 감지. 내용을 작성할 수 없습니다.");
//            reviewRepository.save(review);
//            return review;
//        }
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Review review = new Review(requestDto, productid, nickname, username);
        reviewRepository.save(review);
        return review;
    }

    // Review 수정
    @Transactional
    public String update(Long reviewid, ReviewRequestDto requestDto, String username) {
        Review review = reviewRepository.findById(reviewid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않습니다."));
        String writerId = review.getUsername();
        if (Objects.equals(writerId, username)) {
            review.update(requestDto);
            return "후기 수정 완료";
        }
        return "작성한 유저가 아닙니다.";
    }

    // Review 삭제
    public String deleteReview(Long reviewid, String username) {
        String writerId = reviewRepository.findById(reviewid).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")).getUsername();
        if (Objects.equals(writerId, username)) {
            reviewRepository.deleteById(reviewid);
            return "후기 삭제 완료";
        }
        return "작성한 유저가 아닙니다.";
    }
}