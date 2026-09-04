package com.mbsystems.alertservice.config;

import com.mbsystems.alertservice.model.EmailSubjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertEmailTest {

    @Mock
    private JavaMailSender javaMailSender;

    private AlertEmail alertEmail;

    @BeforeEach
    void setUp() {
        alertEmail = new AlertEmail(javaMailSender);
    }

    @Test
    void testSendEmailDispatchesSimpleMailMessage() {
        EmailSubjects emailSubjects = new EmailSubjects(
                "alert-target@example.com",
                "High Energy Warning",
                "Threshold exceeded by 20%",
                77L
        );

        alertEmail.sendEmail(emailSubjects);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertArrayEquals(new String[]{"alert-target@example.com"}, capturedMessage.getTo());
        assertEquals("noreply@alertservice.com", capturedMessage.getFrom());
        assertEquals("High Energy Warning", capturedMessage.getSubject());
        assertEquals("Threshold exceeded by 20%", capturedMessage.getText());
    }
}
