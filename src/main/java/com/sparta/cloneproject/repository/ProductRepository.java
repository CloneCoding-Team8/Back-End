package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;



public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
//    Product findTopByOrderByIdDesc();

}
//@Query(value = "select created_at from product order by created_at desc limit 1", nativeQuery = true)
//@Query("select p from Product p where p.createdAt order by maxindex()DESC ) ) ")
//Product findByLastProduct();