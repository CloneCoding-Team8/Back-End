package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Bucket;
import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.repository.BucketRepository;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.repository.UserRepository;
import com.sparta.cloneproject.requestdto.BucketRequestDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import com.sparta.cloneproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final ProductRepository productRepository;
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;

    //장바구니 등록
    //장바구니에 이미 있는 상품은 기존 수량에 플러스
    public void addBucket(BucketRequestDto bucketRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Product findProduct = productRepository.findById(bucketRequestDto.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<Bucket> byProductId = bucketRepository.findByProductAndUser(
        productRepository.findById(bucketRequestDto.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
        userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        if(byProductId.isPresent()) {
            int itemCount = byProductId.get().getItemCount();
            itemCount += bucketRequestDto.getItemcount();
            byProductId.get().setItemCount(itemCount);
            bucketRepository.save(byProductId.get());
        } else {
            Bucket bucket = new Bucket();
            bucket.setProduct(findProduct);
            bucket.setUser(user);
            bucket.setItemCount(bucketRequestDto.getItemcount());

            bucketRepository.save(bucket);
        }
    }
    //장바구니 리스트 보기
    public List<BucketResponseDto> myBucketList(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<Bucket> allBucket = bucketRepository.findAllByUser(user);
        List<BucketResponseDto> list = new ArrayList<>();

        for (Bucket bucket : allBucket) {
            BucketResponseDto bucketResponseDto = new BucketResponseDto();
            Product bucketProduct = bucket.getProduct();
            bucketResponseDto.setId(bucket.getId());
            bucketResponseDto.setProductImg(bucketProduct.getProductimg());
            bucketResponseDto.setDeliveryFee(bucketProduct.getDeliveryFee());
            bucketResponseDto.setPrice(bucketProduct.getPrice());
            bucketResponseDto.setTitle(bucketProduct.getTitle());
            bucketResponseDto.setItemCount(bucket.getItemCount());
            bucketResponseDto.setCommaPrice(bucket.getProduct().getCommaPrice());
            bucketResponseDto.setCommaDeliveryFee(bucket.getProduct().getCommaDeliveryFee());

            list.add(bucketResponseDto);
        }
        return list;
    }

    //장바구니 삭제
    public ResponseEntity<?> deleteBucket(long bucketId) {

        bucketRepository.deleteById(bucketId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //장바구니에서 특정상품 수량 변경
    public ResponseEntity<?> changeItemCount(BucketRequestDto bucketRequestDto) {
        Bucket bucket = bucketRepository.findById(bucketRequestDto.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bucket.setItemCount(bucketRequestDto.getItemcount());

        return new ResponseEntity<>(bucketRepository.save(bucket),HttpStatus.NO_CONTENT);
    }

}



//    Optional<Bucket> byProductId = bucketRepository.findByProductLikeAndUser(
//            productRepository.findById(bucketRequestDto.getProductid())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)),
//            userRepository.findById(userDetails.getUser().getId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));


//    //장바구니 등록
//    //장바구니에 이미 있는 상품은 기존 수량에 플러스
//    public void addBucket(BucketRequestDto bucketRequestDto, UserDetailsImpl userDetails) {
//        User user = userRepository.findById(userDetails.getUser().getId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        Product findProduct = productRepository.findById(bucketRequestDto.getProductid())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        Optional<Bucket> byProductId = bucketRepository.findByProductLike(
//                productRepository.findById(bucketRequestDto.getProductid())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
//
//        if(byProductId.isPresent()) {
//            int itemCount = byProductId.get().getItemCount();
//            itemCount += bucketRequestDto.getItemcount();
//            byProductId.get().setItemCount(itemCount);
//            bucketRepository.save(byProductId.get());
//        } else {
//            Bucket bucket = new Bucket();
//            bucket.setProduct(findProduct);
//            bucket.setUser(user);
//            bucket.setItemCount(bucketRequestDto.getItemcount());
//
//            bucketRepository.save(bucket);