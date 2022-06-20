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
        name = "BUCKET_ID_GENERATOR",
        sequenceName = "BUCKET_SEQUENCES",
        initialValue = 1, allocationSize = 1
)
@Table(name = "BUCKET")
public class Bucket {

    @Id
    @Column(name = "bucket_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "BUCKET_ID_GENERATOR")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int itemCount;



}
