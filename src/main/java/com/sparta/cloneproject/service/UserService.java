package com.sparta.cloneproject.service;

import com.sparta.cloneproject.exception.AuthenticationException;
import com.sparta.cloneproject.exception.ErrorCode;
import com.sparta.cloneproject.model.Auth;
import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.model.UserRoleEnum;
import com.sparta.cloneproject.repository.AuthRepository;
import com.sparta.cloneproject.repository.UserRepository;
import com.sparta.cloneproject.requestdto.RefreshTokenRequestDto;
import com.sparta.cloneproject.requestdto.UserRequestDto;
import com.sparta.cloneproject.responsedto.JwtResponseDto;
import com.sparta.cloneproject.responsedto.ResponseMap;
import com.sparta.cloneproject.security.JWT.JwtTokenProvider;
import com.sparta.cloneproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    //회원가입
    public HttpStatus signupUser(UserRequestDto singUpData) {
        UserRoleEnum role = UserRoleEnum.USER;

        User beforeSaveUser = new User(singUpData, role);
        beforeSaveUser.encryptPassword(passwordEncoder);
        userRepository.save(beforeSaveUser);
        return HttpStatus.CREATED;
    }

    //아이디 중복 검사
    public boolean signupUseridCheck(UserRequestDto signupUseridCheckData) {
        Optional<User> found = userRepository.findByUsername(signupUseridCheckData.getUsername());
        return !found.isPresent();
    }

    //로그인
    public JwtResponseDto loginUser(UserRequestDto loginData) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));

        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.UsernameOrPasswordNotFoundException);
        }

        return createJwtToken(authentication);
    }

    //Access 토큰 재발급
    public JwtResponseDto newAccessToken(RefreshTokenRequestDto refreshTokenRequestDto){
        JwtResponseDto jwtResponseDto = new JwtResponseDto();
        ResponseMap result = new ResponseMap();

        Auth dbrefreshtoken = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken());
        try{
            // AccessToken은 만료되었지만 RefreshToken은 만료되지 않은 경우
            if (jwtTokenProvider.validateToken(dbrefreshtoken.getRefreshtoken())) {

                User user = userRepository.findByUsername(dbrefreshtoken.getUsername()).orElse(null);

                String accessToken = jwtTokenProvider.createnewAccessToken(user);
                jwtResponseDto.setAccesstoken(accessToken);
                jwtResponseDto.setTokenmessage("새로운 토큰이 발급되었습니다.");

//                result.setResponseData("accessToken", accessToken);
//                result.setResponseData("message", "새로운 토큰이 발급되었습니다.");
            } else {
                // RefreshToken 또한 만료된 경우는 로그인을 다시 진행해야 한다.
                jwtResponseDto.setCode(ErrorCode.ReLogin.getCode());
                jwtResponseDto.setErrormessage(ErrorCode.ReLogin.getMessage());
                jwtResponseDto.setErrorststus(ErrorCode.ReLogin.getStatus());

//                result.setResponseData("code", ErrorCode.ReLogin.getCode());
//                result.setResponseData("message", ErrorCode.ReLogin.getMessage());
//                result.setResponseData("HttpStatus", ErrorCode.ReLogin.getStatus());
                Auth auth = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken());
                authRepository.delete(auth);
            }
        } catch (NullPointerException e) {
            //RefreshToken이 잘못되어있거나 없는 경우
            jwtResponseDto.setCode(ErrorCode.UNAUTHORIZEDException.getCode());
            jwtResponseDto.setErrormessage(ErrorCode.UNAUTHORIZEDException.getMessage());
            jwtResponseDto.setErrorststus(ErrorCode.UNAUTHORIZEDException.getStatus());

//            result.setResponseData("code", ErrorCode.UNAUTHORIZEDException.getCode());
//            result.setResponseData("message", ErrorCode.UNAUTHORIZEDException.getMessage());
//            result.setResponseData("HttpStatus", ErrorCode.UNAUTHORIZEDException.getStatus());
        }
        return jwtResponseDto;
    }

    //로그아웃
    public void logout(RefreshTokenRequestDto refreshTokenRequestDto) {
        Auth auth = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken());
        authRepository.delete(auth);
    }

    //JWT 토큰 생성기
    private JwtResponseDto createJwtToken(Authentication authentication) {
        JwtResponseDto jwtResponseDto = new JwtResponseDto();
//        ResponseMap result = new ResponseMap();

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        Auth auth = new Auth(principal.getUsername(), refreshToken);
        authRepository.save(auth);

        jwtResponseDto.setCode(201);
        jwtResponseDto.setAccesstoken(accessToken);
        jwtResponseDto.setRefreshtoken(refreshToken);

//        result.setResponseData("accessToken", accessToken);
//        result.setResponseData("refreshToken", refreshToken);

        return jwtResponseDto;
    }
}