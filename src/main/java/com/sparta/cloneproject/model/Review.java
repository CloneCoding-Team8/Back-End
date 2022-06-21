package com.sparta.cloneproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "REVIEW_ID_GENERATOR",
        sequenceName = "REVIEW_SEQUENCES",
        initialValue = 1, allocationSize = 1
)
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String itemImg;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int star;


}
