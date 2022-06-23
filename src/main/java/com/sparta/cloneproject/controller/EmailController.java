package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.requestdto.MailDto;
import com.sparta.cloneproject.requestdto.UserMailRequestDto;
import com.sparta.cloneproject.service.MailService;
import com.sparta.cloneproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final UserService userService;
    private final MailService emailService;

//    @PostMapping("/mail/send")
//    public void sendMail(MailDto mailDto) throws MessagingException {
//        emailService.sendSimpleMessage(mailDto);
//        System.out.println("메일 전송 완료");
//    }

    @PostMapping("/user/mail/send")
    public ResponseEntity<?> sendUserPassword(@RequestBody UserMailRequestDto userMailRequestDto) throws MessagingException {
        return userService.sendUserPassword(userMailRequestDto);
    }
}
