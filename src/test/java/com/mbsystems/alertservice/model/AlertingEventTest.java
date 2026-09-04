package com.mbsystems.alertservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertingEventTest {

    @Test
    void testAlertingEventCreationAndAccessors() {
        AlertingEvent event = new AlertingEvent(
                123L,
                "High consumption detected",
                50.0,
                75.5,
                "user@example.com"
        );

        assertEquals(123L, event.userId());
        assertEquals("High consumption detected", event.message());
        assertEquals(50.0, event.threshold());
        assertEquals(75.5, event.energyConsumed());
        assertEquals("user@example.com", event.email());
    }

    @Test
    void testAlertingEventEqualsHashCodeToString() {
        AlertingEvent event1 = new AlertingEvent(123L, "Alert", 50.0, 75.5, "user@example.com");
        AlertingEvent event2 = new AlertingEvent(123L, "Alert", 50.0, 75.5, "user@example.com");
        AlertingEvent event3 = new AlertingEvent(456L, "Alert", 50.0, 75.5, "user@example.com");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
        assertTrue(event1.toString().contains("123"));
        assertTrue(event1.toString().contains("user@example.com"));
    }
}
