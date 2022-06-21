package com.sparta.cloneproject.security.JWT;

import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserDetailsServiceImpl userDetailsService;

    // jwt 시크릿 키
    private String secretKey = "A1000forStoreZ";

<<<<<<< HEAD
    private final long accessTokenValidTime = 30 * 60 * 1000L;   // access 토큰 유효시간 5분
=======
    private final long accessTokenValidTime = 10 * 60 * 1000L;   // access 토큰 유효시간 5분
>>>>>>> 90808272626ef178a61849b09893a69837e96393
    private final long refreshTokenValidTime = 60 * 60 * 1000L; // refresh 토큰 유효시간 30분

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Access 토큰 생성
    public String createnewAccessToken(User user) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", user.getUsername());
        payloads.put("Role", user.getRole());
        payloads.put("nickname",user.getNickname());

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(user.getUsername())

                //토큰 생성 시간
                .setIssuedAt(new Date(System.currentTimeMillis()))

                //토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))

                //토큰 암호화
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(User user) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", user.getUsername());

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(user.getUsername())

                //토큰 생성 시간
                .setIssuedAt(new Date(System.currentTimeMillis()))

                //토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))

                //토큰 암호화
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옴.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    // 토큰의 유효성 + 만료일자 확인 + 인증 예외 처리 + 권한 에러 처리
    public boolean validateJwtToken(ServletRequest request, String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return true;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", "IllegalArgumentException");
        }
        return false;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}