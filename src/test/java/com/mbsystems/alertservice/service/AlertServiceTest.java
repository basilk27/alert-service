package com.mbsystems.alertservice.service;

import com.mbsystems.alertservice.config.AlertEmail;
import com.mbsystems.alertservice.entity.Alert;
import com.mbsystems.alertservice.function.EmailMessageFunction;
import com.mbsystems.alertservice.model.AlertingEvent;
import com.mbsystems.alertservice.model.EmailSubjects;
import com.mbsystems.alertservice.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertEmail alertEmail;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private EmailMessageFunction emailMessageFunction;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(alertEmail, alertRepository, emailMessageFunction);
    }

    @Test
    void testEnergyUsageAlertEventSendsEmailAndSavesAlert() {
        AlertingEvent event = new AlertingEvent(
                55L,
                "Overuse detected",
                20.0,
                35.0,
                "user55@test.com"
        );

        EmailSubjects expectedEmailSubjects = new EmailSubjects(
                "user55@test.com",
                "Energy Usage Alert for User 55",
                "Alert: Overuse detected\nThreshold: 20.0\nEnergy Consumed:35.0",
                55L
        );

        when(emailMessageFunction.apply(event)).thenReturn(expectedEmailSubjects);

        alertService.energyUsageAlertEvent(event);

        verify(emailMessageFunction, times(1)).apply(event);
        verify(alertEmail, times(1)).sendEmail(expectedEmailSubjects);

        ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);
        verify(alertRepository, times(1)).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();
        assertNotNull(savedAlert);
        assertEquals(55L, savedAlert.userId());
        assertTrue(savedAlert.sent());
        assertNotNull(savedAlert.createdAt());
    }
}
