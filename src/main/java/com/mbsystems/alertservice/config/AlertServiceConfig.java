package com.mbsystems.alertservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AlertServiceConfig {

    @Bean
    public AlertEmail alertEmail(JavaMailSender javaMailSender) {
        return new AlertEmail(javaMailSender);
    }
}
