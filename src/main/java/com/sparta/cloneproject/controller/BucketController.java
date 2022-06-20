package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.requestdto.BucketRequestDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import com.sparta.cloneproject.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;



    @PostMapping("/api/product/item")
    public void saveBucket(@RequestBody BucketRequestDto bucketRequestDto) {
        bucketService.addBucket(bucketRequestDto);
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
    public void changeItemCount(@RequestBody BucketRequestDto bucketRequestDto) {
        bucketService.changeItemCount(bucketRequestDto);
    }
}
