package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.repository.BucketRepository;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.requestdto.BucketRequsetDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import com.sparta.cloneproject.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BucketController {
    //private final ProductRepository productRepository;
    private final BucketService bucketService;
    //private final BucketRepository bucketRepository;



    @PostMapping("/api/product/item")
    public void saveBucket(@RequestBody BucketRequsetDto bucketRequsetDto) {
        bucketService.addBucket(bucketRequsetDto);
    }

    @GetMapping("/api/product/itemcheck")
    public List<BucketResponseDto> myBucketList() {
        return bucketService.myBucketList();
    }

    @DeleteMapping("/api/product/{productid}")
    public void deleteBucket(@PathVariable Long productid) {
        bucketService.deleteBucket(productid);
    }

    @PatchMapping("/api/product/itemcount")
    public void changeItemCount(@RequestBody BucketRequsetDto bucketRequsetDto) {
        bucketService.changeItemCount(bucketRequsetDto);
    }
}
