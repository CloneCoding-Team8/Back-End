package com.sparta.cloneproject.model;

import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
@Table(name = "REVIEW")
public class Review extends Timestamped {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private Long productid;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int star;

    @Column(nullable = false)
    private String reviewimg;

    public Review(Long productid, ReviewRequestDto requestDto, Map<String , String> reviewimg , String nickname, String username) {
        this.productid = productid;
        this.content = requestDto.getContent();
        this.star = requestDto.getStar();
        this.reviewimg = reviewimg.get("url");
        this.username = username;
        this.nickname = nickname;
    }

    public void reviewUpdate(ReviewRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.star = requestDto.getStar();
    }
}