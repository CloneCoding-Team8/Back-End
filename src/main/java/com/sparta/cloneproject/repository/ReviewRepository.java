package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductidOrderByCreatedAtDesc(Long productid);
}