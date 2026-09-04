package com.mbsystems.alertservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void testCreateAlert() {
        Long userId = 101L;
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        Alert alert = Alert.create(userId);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNull(alert.id());
        assertEquals(userId, alert.userId());
        assertTrue(alert.sent());
        assertNotNull(alert.createdAt());
        assertTrue(alert.createdAt().isAfter(before) || alert.createdAt().isEqual(before));
        assertTrue(alert.createdAt().isBefore(after) || alert.createdAt().isEqual(after));
    }

    @Test
    void testAlertConstructorAndAccessors() {
        LocalDateTime now = LocalDateTime.now();
        Alert alert = new Alert(1L, 202L, now, false);

        assertEquals(1L, alert.id());
        assertEquals(202L, alert.userId());
        assertEquals(now, alert.createdAt());
        assertFalse(alert.sent());
    }

    @Test
    void testAlertEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Alert alert1 = new Alert(1L, 202L, now, true);
        Alert alert2 = new Alert(1L, 202L, now, true);
        Alert alert3 = new Alert(2L, 202L, now, true);

        assertEquals(alert1, alert2);
        assertEquals(alert1.hashCode(), alert2.hashCode());
        assertNotEquals(alert1, alert3);
        assertNotNull(alert1.toString());
    }
}
