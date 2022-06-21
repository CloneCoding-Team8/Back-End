package com.sparta.cloneproject.model;

import com.sparta.cloneproject.requestdto.ReviewRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
@SequenceGenerator(
        name = "REVIEW_ID_GENERATOR",
        sequenceName = "REVIEW_SEQUENCES",
        initialValue = 1, allocationSize = 1
)
@Table(name = "REVIEW")
public class Review extends Timestamped {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long reviewid;

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

    private String itemimg;

    public Review(ReviewRequestDto requestDto, Long productid,String nickname,String username) {
        this.productid = productid;
        this.nickname = nickname;
        this.content = requestDto.getContent();
        this.username = username;
        this.star = requestDto.getStar();
    }

//    public Review(ReviewRequestDto requestDto, String content, Long productid,String nickname,String username) {
//        this.productid = productid();
//        this.nickname = pickname();
//        this.content = content;
//        this.username = psername();
//        this.star = requestDto.getStar();
//    }

    public void update(ReviewRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}

