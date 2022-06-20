package com.sparta.cloneproject.security.JWT;

import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.model.UserRoleEnum;
import com.sparta.cloneproject.security.UserDetailsImpl;
import com.sparta.cloneproject.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
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

    private final long accessTokenValidTime = 1 * 60 * 1000L;   // access 토큰 유효시간 5분
    private final long refreshTokenValidTime = 2 * 60 * 1000L; // refresh 토큰 유효시간 30분

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createnewAccessToken(User user) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", user.getUsername());
        payloads.put("Role", user.getRole());
        payloads.put("nickname",user.getNickname());

        String jwt = Jwts.builder()
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

        return jwt;
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {

        Map<String, Object> claims = new HashMap<>();

        val isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + UserRoleEnum.ADMIN));
        if (isAdmin) {
            claims.put("Role","ADMIN");
        } else {
            claims.put("Role","USER");
        }

        val username = userDetails.getUsername();
        claims.put("Username", username);

        val nickname = userDetails.getNickname();
        claims.put("nickname", nickname);

        return doGenerateAccessToken(claims, userDetails.getUsername());
    }

    private String doGenerateAccessToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(subject)

                //토큰 생성 시간
                .setIssuedAt(new Date(System.currentTimeMillis()))

                //토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))

                //토큰 암호화
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {

        Map<String, Object> claims = new HashMap<>();

        val username = userDetails.getUsername();
        claims.put("Username", username);

        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setSubject(subject)

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
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
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

    //refresh token 정보 얻어내기
    public Claims getClaimsFromJwtToken(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
    }
}