package com.bibek.bdfs.mail;

import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.otp.entity.OTP;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async
    @Override
    public void sendForgotPasswordMail(User userEntity, String forgotPasswordUrl, LocalDateTime expiry) {
        String email = userEntity.getEmailId();
        String name = userEntity.getFullName();

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", forgotPasswordUrl);
        context.setVariable("expiryTime", expiry.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setVariable("email", email);

        String content = templateEngine.process("forgot-password-link.html", context);
        sendEmail(email, "Reset your password", content);
    }

    @Override
    public void sendRegistrationMail(User userEntity, OTP otp, URI frontEndUri) {
        String email = userEntity.getEmailId();
        String name = userEntity.getFullName();

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp.getOtpValue());
        context.setVariable("expiryTime", otp.getExpiryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setVariable("email", email);
        context.setVariable("frontEndUri", frontEndUri.toString());

        String content = templateEngine.process("registration-otp.html", context);
        sendEmail(email, "Verify your email address", content);
    }

    @Override
    public void bloodRequestNotificationMail(User userEntity, String message, LocalDateTime expiry) {
        String email = userEntity.getEmailId();
        String name = userEntity.getFullName();
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("message", message);
        context.setVariable("expiryTime", expiry.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setVariable("email", email);

        String content = templateEngine.process("blood-request-notification.html", context);
        sendEmail(email, "New Blood Request Notification", content);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@openmichub.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending mail to {}: {}", to, e.getMessage());
            throw new RuntimeException("Error sending mail", e);
        }
    }

}
