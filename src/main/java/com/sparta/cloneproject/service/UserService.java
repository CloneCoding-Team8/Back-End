package com.sparta.cloneproject.service;

import com.sparta.cloneproject.exception.AuthenticationException;
import com.sparta.cloneproject.exception.ErrorCode;
import com.sparta.cloneproject.model.Auth;
import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.model.UserRoleEnum;
import com.sparta.cloneproject.repository.AuthRepository;
import com.sparta.cloneproject.repository.UserRepository;
import com.sparta.cloneproject.requestdto.RefreshTokenRequestDto;
import com.sparta.cloneproject.requestdto.UserMailRequestDto;
import com.sparta.cloneproject.requestdto.UserRequestDto;
import com.sparta.cloneproject.responsedto.JwtResponseDto;
import com.sparta.cloneproject.security.JWT.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
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
    private final MailService emailService;

    //회원가입
    public ResponseEntity<?> signupUser(UserRequestDto singUpData) {
        UserRoleEnum role = UserRoleEnum.USER;

        User beforeSaveUser = new User(singUpData, role);
        beforeSaveUser.encryptPassword(passwordEncoder);
        userRepository.save(beforeSaveUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //아이디 중복 검사
    public boolean signupUseridCheck(UserRequestDto signupUseridCheckData) {
        Optional<User> found = userRepository.findByUsername(signupUseridCheckData.getUsername());
        return !found.isPresent();
    }

    //로그인
    public JwtResponseDto loginUser(UserRequestDto loginData) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
        User user = userRepository.findByUsername(loginData.getUsername()).orElse(null);
        return createJwtToken(user);
    }

    //비밀번호 찾기(e-mail 발송)
    public ResponseEntity<?> sendUserPassword(UserMailRequestDto userMailRequestDto) throws MessagingException {
        System.out.println(userMailRequestDto.getUsername());
        System.out.println(userMailRequestDto.getUseremail());
        try {
            userRepository.findByUsername(userMailRequestDto.getUsername());
            emailService.sendUserPassword(userMailRequestDto);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("아이디와 이메일을 확인해 주세요",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Access 토큰 재발급
    public JwtResponseDto newAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        JwtResponseDto jwtResponseDto = new JwtResponseDto();

        Auth dbrefreshtoken = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken().substring(13));
        try {
            // AccessToken은 만료되었지만 RefreshToken은 만료되지 않은 경우
            if (jwtTokenProvider.validateToken(dbrefreshtoken.getRefreshtoken())) {

                User user = userRepository.findByUsername(dbrefreshtoken.getUsername()).orElse(null);

                String accessToken = jwtTokenProvider.createnewAccessToken(user);
                jwtResponseDto.setAccesstoken(accessToken);
                jwtResponseDto.setMessage("새로운 Access토큰이 발급되었습니다.");

            } else {
                // RefreshToken 또한 만료된 경우는 로그인을 다시 진행해야 한다.
                jwtResponseDto.setCode(ErrorCode.ReLogin.getCode());
                jwtResponseDto.setMessage(ErrorCode.ReLogin.getMessage());
                jwtResponseDto.setErrorststus(ErrorCode.ReLogin.getStatus());

                Auth auth = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken());
                authRepository.delete(auth);
            }
        } catch (NullPointerException e) {
            //RefreshToken이 잘못되어있거나 없는 경우
            jwtResponseDto.setCode(ErrorCode.UNAUTHORIZEDException.getCode());
            jwtResponseDto.setMessage(ErrorCode.UNAUTHORIZEDException.getMessage());
            jwtResponseDto.setErrorststus(ErrorCode.UNAUTHORIZEDException.getStatus());

        }
        return jwtResponseDto;
    }

    //로그아웃
    public ResponseEntity<?> logout(RefreshTokenRequestDto refreshTokenRequestDto) {
        Auth auth = authRepository.findByRefreshtoken(refreshTokenRequestDto.getRefreshtoken().substring(13));
        authRepository.delete(auth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //JWT 토큰 생성기
    private JwtResponseDto createJwtToken(User user) {
        JwtResponseDto jwtResponseDto = new JwtResponseDto();

        String accessToken = jwtTokenProvider.createnewAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        Auth auth = new Auth(user.getUsername(), refreshToken);
        authRepository.save(auth);

        jwtResponseDto.setCode(201);
        jwtResponseDto.setAccesstoken(accessToken);
        jwtResponseDto.setRefreshtoken(refreshToken);
        jwtResponseDto.setMessage("Access토큰과 Refresh토큰이 발급되었습니다.");

        return jwtResponseDto;
    }
}