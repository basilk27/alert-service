package com.mbsystems.alertservice.function;

import com.mbsystems.alertservice.model.AlertingEvent;
import com.mbsystems.alertservice.model.EmailSubjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailMessageFunctionTest {

    private EmailMessageFunction emailMessageFunction;

    @BeforeEach
    void setUp() {
        emailMessageFunction = new EmailMessageFunction();
    }

    @Test
    void testApplyMapsAlertingEventToEmailSubjects() {
        AlertingEvent event = new AlertingEvent(
                42L,
                "Exceeded daily threshold",
                100.0,
                125.5,
                "resident42@example.com"
        );

        EmailSubjects result = emailMessageFunction.apply(event);

        assertNotNull(result);
        assertEquals("resident42@example.com", result.to());
        assertEquals("Energy Usage Alert for User 42", result.subject());
        String expectedBody = "Alert: Exceeded daily threshold\nThreshold: 100.0\nEnergy Consumed:125.5";
        assertEquals(expectedBody, result.body());
        assertEquals(42L, result.userId());
    }
}
