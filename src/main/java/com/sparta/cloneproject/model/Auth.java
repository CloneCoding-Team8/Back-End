package com.sparta.cloneproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Auth {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String refreshtoken;

    public Auth(String username, String refreshtoken){
        this.username = username;
        this.refreshtoken = refreshtoken;
    }
}
