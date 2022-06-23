package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.requestdto.BucketRequestDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import com.sparta.cloneproject.security.UserDetailsImpl;
import com.sparta.cloneproject.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;

    @PostMapping("/api/product/item")
    public void saveBucket(@RequestBody BucketRequestDto bucketRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bucketService.addBucket(bucketRequestDto, userDetails);
    }

    @GetMapping("/api/product/itemcheck")
    public List<BucketResponseDto> myBucketList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return bucketService.myBucketList(userDetails);
    }

    @DeleteMapping("/api/product/{productid}")
    public void deleteBucket(@PathVariable Long productid) {
        bucketService.deleteBucket(productid);
    }

    @PatchMapping("/api/product/itemcount")
    public void changeItemCount(@RequestBody BucketRequestDto bucketRequestDto) {
        bucketService.changeItemCount(bucketRequestDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
