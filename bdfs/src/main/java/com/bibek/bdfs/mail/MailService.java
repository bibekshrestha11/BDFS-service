package com.bibek.bdfs.mail;

import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.otp.entity.OTP;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;
import java.time.LocalDateTime;

public interface MailService {

    @Async
    void sendForgotPasswordMail(User userEntity, String forgotPasswordUrl, LocalDateTime expiry);

    @Async
    void sendRegistrationMail(User userEntity, OTP otp, URI frontEndUri);

}
