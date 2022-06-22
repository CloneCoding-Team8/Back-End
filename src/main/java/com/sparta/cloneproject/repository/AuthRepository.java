package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Auth findByRefreshtoken(String refreshtoken);
    Auth findByUsername(String username);
}
