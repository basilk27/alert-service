package com.mbsystems.alertservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailSubjectsTest {

    @Test
    void testEmailSubjectsCreationAndAccessors() {
        EmailSubjects emailSubjects = new EmailSubjects(
                "recipient@example.com",
                "Test Subject",
                "Test Body",
                999L
        );

        assertEquals("recipient@example.com", emailSubjects.to());
        assertEquals("Test Subject", emailSubjects.subject());
        assertEquals("Test Body", emailSubjects.body());
        assertEquals(999L, emailSubjects.userId());
    }

    @Test
    void testEmailSubjectsEqualsHashCodeToString() {
        EmailSubjects subject1 = new EmailSubjects("to@test.com", "Sub", "Body", 1L);
        EmailSubjects subject2 = new EmailSubjects("to@test.com", "Sub", "Body", 1L);
        EmailSubjects subject3 = new EmailSubjects("other@test.com", "Sub", "Body", 2L);

        assertEquals(subject1, subject2);
        assertEquals(subject1.hashCode(), subject2.hashCode());
        assertNotEquals(subject1, subject3);
        assertTrue(subject1.toString().contains("to@test.com"));
        assertTrue(subject1.toString().contains("Sub"));
    }
}
