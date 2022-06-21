package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Bucket;
import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByProductLike(Product product);
    Optional<Bucket> findByProductAndUser(Product product, User user);
    List<Bucket> findAllByUser(User user);
}
