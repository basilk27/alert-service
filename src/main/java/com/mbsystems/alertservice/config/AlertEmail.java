package com.mbsystems.alertservice.config;

import com.mbsystems.alertservice.model.EmailSubjects;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AlertEmail {

    private static final String SENT_FROM = "noreply@alertservice.com";
    private final JavaMailSender javaMailSender;

    private static final Function<EmailSubjects, SimpleMailMessage> emailSubjectsFun = emailSubjects -> {
        SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailSubjects.to());
            message.setFrom(SENT_FROM);
            message.setSubject(emailSubjects.subject());
            message.setText(emailSubjects.body());

        return message;
    };

    public void sendEmail(EmailSubjects emailSubjects) {
        SimpleMailMessage message = emailSubjectsFun.apply(emailSubjects);

        javaMailSender.send(message);
    }
}
