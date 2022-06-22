package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.requestdto.BucketRequestDto;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import com.sparta.cloneproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //상품추가용 api
    @PostMapping("/api/product")
    public void createProduct(@RequestPart(value = "img",required = false) MultipartFile multipartFile,
                              @RequestPart(value = "productRequestDto") ProductRequestDto productRequestDto) {

        productService.createProduct(productRequestDto, multipartFile);
    }

    @GetMapping("/api/product/list/{productid}")
    public Optional<Product> getProductDetails(@PathVariable Long productid) {
        return productService.getProductDetails(productid);
    }

    @GetMapping("/api/product/list")
    public Page<Product> getAllProductList(@PageableDefault(
                        size = 3,
                        sort = "createdAt",
                        direction = Sort.Direction.DESC) Pageable pageable) {

        return productService.getAllProductList(pageable);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleException(MultipartException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 파일 형식입니다.");
    }
}
