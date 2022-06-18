package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Bucket;
import com.sparta.cloneproject.model.Product;
import com.sparta.cloneproject.repository.BucketRepository;
import com.sparta.cloneproject.repository.ProductRepository;
import com.sparta.cloneproject.requestdto.BucketRequsetDto;
import com.sparta.cloneproject.requestdto.ProductRequestDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final ProductRepository productRepository;
    private final BucketRepository bucketRepository;

    //장바구니 등록
    //장바구니에 이미 있는 상품은 기존 수량에 플러스
    public void addBucket(BucketRequsetDto bucketRequestDto) {

        Product findProduct = productRepository.findById(bucketRequestDto.getProductid()).orElseThrow(null);
        Optional<Bucket> byProductId = bucketRepository.findByProductLike(productRepository.findById(bucketRequestDto.getProductid()).orElseThrow(null));

        if(byProductId.isPresent()) {
            int itemCount = byProductId.get().getItemCount();
            itemCount += bucketRequestDto.getItemcount();
            byProductId.get().setItemCount(itemCount);
            bucketRepository.save(byProductId.get());
        } else {
            Bucket bucket = new Bucket();
            bucket.setProduct(findProduct);
            bucket.setItemCount(bucketRequestDto.getItemcount());

            bucketRepository.save(bucket);
        }
    }
    //장바구니 리스트 보기
    public List<BucketResponseDto> myBucketList() {
        List<Bucket> allBucket = bucketRepository.findAll();
        List<BucketResponseDto> list = new ArrayList<>();

        for (Bucket bucket : allBucket) {
            BucketResponseDto bucketResponseDto = new BucketResponseDto();
            Product bucketProduct = bucket.getProduct();
            bucketResponseDto.setId(bucket.getId());
            bucketResponseDto.setProductimg(bucketProduct.getProductimg());
            bucketResponseDto.setDelivery(bucketProduct.getDeliveryFee());
            bucketResponseDto.setPrice(bucketProduct.getPrice());
            bucketResponseDto.setTitle(bucketProduct.getTitle());
            bucketResponseDto.setItemcount(bucket.getItemCount());

            list.add(bucketResponseDto);
        }
        return list;
    }

    //장바구니 삭제
    public void deleteBucket(long bucketId) {
        Bucket bucket = bucketRepository.findById(bucketId).orElseThrow(null);
        bucketRepository.delete(bucket);
    }

    //장바구니에서 특정상품 수량 변경
    public void changeItemCount(BucketRequsetDto bucketRequsetDto) {
        Bucket bucket = bucketRepository.findById(bucketRequsetDto.getProductid()).orElseThrow(null);
        bucket.setItemCount(bucketRequsetDto.getItemcount());

        bucketRepository.save(bucket);
    }

}
