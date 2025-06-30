package com.bibek.bdfs.mail;

import com.bibek.bdfs.user.entity.User;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

public interface MailService {

    @Async
    void sendOtpEmail(String to, String name, String otp, LocalDateTime expiry);

    @Async
    void sendForgotPasswordMail(User userEntity, String forgotPasswordUrl, LocalDateTime expiry);
}
