package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Bucket;
import com.sparta.cloneproject.model.User;
import com.sparta.cloneproject.repository.BucketRepository;
import com.sparta.cloneproject.repository.UserRepository;
import com.sparta.cloneproject.requestdto.MailDto;
import com.sparta.cloneproject.requestdto.UserMailRequestDto;
import com.sparta.cloneproject.responsedto.BucketResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class MailService {
    private final BucketRepository bucketRepository;
    private static final String FROM_ADDRESS = "noreply@baeldung.com";
    // Java 메일
    private final JavaMailSender emailSender;
    private final TemplateEngine htmlTemplateEngine;
    private final UserRepository userRepository;

    public void sendUserPassword(UserMailRequestDto userMailRequestDto) throws MessagingException {
        User user = userRepository.findByUsername(userMailRequestDto.getUsername()).orElse(null);
        String variables = user.getPassword();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MailService.FROM_ADDRESS);
        message.setTo(userMailRequestDto.getUseremail());
        message.setSubject("요청하신 비밀번호 입니다");
        message.setText(variables);
        emailSender.send(message);
    }

//    public void sendSimpleMessage(MailDto mailDto) throws MessagingException {
//        List<BucketResponseDto> variables = myOrderList();
//        Context context = new Context();
//        context.setVariable("text", variables);
//
//        String htmlTemplate = htmlTemplateEngine.process("/mail-template", context);
//
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
//        messageHelper.setFrom(FROM_ADDRESS);
//        messageHelper.setTo(mailDto.getAddress());
//        messageHelper.setSubject(mailDto.getTitle());
//        messageHelper.setText(htmlTemplate, Boolean.TRUE);
//        emailSender.send(mimeMessage);
//    }

//    public List<BucketResponseDto> myOrderList() {
//        List<Bucket> all = bucketRepository.findAll();
//        List<BucketResponseDto> list = new ArrayList<>();
//        BucketResponseDto bucketResponseDto = new BucketResponseDto();
//        for (int i = 0; i < all.size(); i++) {
//            bucketResponseDto.setId(all.get(i).getId());
//            bucketResponseDto.setTitle(all.get(i).getProduct().getTitle());
//            bucketResponseDto.setPrice(all.get(i).getProduct().getPrice());
//            bucketResponseDto.setItemcount(all.get(i).getItemCount());
//            bucketResponseDto.setDelivery(all.get(i).getProduct().getDeliveryFee());
//            list.add(bucketResponseDto);
//        }
//        return list;
//    }
}

//    public void sendSimpleMessage(MailDto mailDto) {
//        List<BucketResponseDto> variables = myOrderList();
//        Context context = new Context();
//        context.setVariables((Map<String, Object>) variables);
//
//        String htmlTemplate = htmlTemplateEngine.process("/mail-template", context);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(MailService.FROM_ADDRESS);
//        message.setTo(mailDto.getAddress());
//        message.setSubject(mailDto.getTitle());
//        message.setText(mailDto.getContent());
//        emailSender.send(message);
//    }
