package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByCreatedAtDesc();

}
