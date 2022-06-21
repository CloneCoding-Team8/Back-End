package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Review;
import com.sparta.cloneproject.repository.ReviewRepository;
import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
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
    public Review createReview(Long productid, MultipartFile itemimg,
                               ReviewRequestDto requestDto,
                               String nickname,
                               String username) {
        Map<String, String> reviewimg = s3Service.uploadFile(itemimg);
        Review review = new Review(productid, requestDto, reviewimg, nickname, username);
        reviewRepository.save(review);
        return review;
    }

    // Review 수정
    @Transactional
    public String updateReview(Long reviewid, ReviewRequestDto requestDto, String username) {
        Review review = reviewRepository.findById(reviewid).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않습니다."));
        String writerId = review.getUsername();
        if (Objects.equals(writerId, username)) {
            review.reviewUpdate(requestDto);
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