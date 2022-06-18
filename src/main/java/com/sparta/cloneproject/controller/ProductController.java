package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import com.sparta.cloneproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @PostMapping("/api/product")
    public void createProduct(@RequestPart(value = "img",required = false) MultipartFile multipartFile,
                              @RequestPart(value = "productRequestDto") ProductRequestDto productRequestDto) {

        productService.createProduct(productRequestDto,multipartFile);
    }

    @GetMapping("/api/product/{productid}")
    public Optional<Product> getProductDetails(@PathVariable Long productid) {
        return productService.getProductDetails(productid);
    }

    @GetMapping("/api/product")
    public List<Product> getAllProductList() {
        return productService.getAllProductList();
    }
}
