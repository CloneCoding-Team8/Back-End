package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import com.sparta.cloneproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //상품추가용 api

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
    public Page<Product> getAllProductList(@PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
//            @RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sortBy") String sortBy, @RequestParam("isAsc") boolean isAsc
        ) {

//        page = page - 1;

//        return productService.getAllProductList(page, size, sortBy, isAsc);
        return productService.getAllProductList(pageable);
    }
}
