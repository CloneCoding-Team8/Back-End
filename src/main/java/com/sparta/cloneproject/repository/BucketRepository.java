package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Bucket;
import com.sparta.cloneproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByProductLike(Product product);
}
