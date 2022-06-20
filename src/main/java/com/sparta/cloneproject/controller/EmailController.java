package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.requestdto.MailDto;
import com.sparta.cloneproject.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final MailService emailService;

    @PostMapping("/mail/send")
    public void sendMail(MailDto mailDto) throws MessagingException {
        emailService.sendSimpleMessage(mailDto);
        System.out.println("메일 전송 완료");
    }
}
