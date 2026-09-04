package com.mbsystems.alertservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AlertServiceConfigTest {

    @Test
    void testAlertEmailBeanCreation() {
        AlertServiceConfig config = new AlertServiceConfig();
        JavaMailSender mailSender = mock(JavaMailSender.class);

        AlertEmail alertEmail = config.alertEmail(mailSender);

        assertNotNull(alertEmail);
    }
}
