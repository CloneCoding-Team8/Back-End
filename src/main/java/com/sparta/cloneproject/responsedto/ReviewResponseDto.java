package com.sparta.cloneproject.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.cloneproject.model.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String nickname;
    private String username;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private final LocalDateTime modified;

//    private String itemimg;
    private String content;

    private int star;

    @Builder
    public ReviewResponseDto(Review content) {
        this.id = content.getReviewid();
        this.nickname = content.getNickname();
        this.username = content.getUsername();
        this.modified = content.getModifiedAt();
        this.content = content.getContent();
        this.star = content.getStar();
    }
}
