package com.mbsystems.alertservice.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlertMailPropsTest {

    @Test
    void testAlertMailPropsCreationAndAccessors() {
        Map<String, String> properties = Map.of(
                "mail.smtp.auth", "false",
                "mail.smtp.starttls.enable", "false"
        );
        AlertMailProps props = new AlertMailProps("localhost", 1025, properties);

        assertEquals("localhost", props.host());
        assertEquals(1025, props.port());
        assertEquals(properties, props.properties());
        assertEquals("false", props.properties().get("mail.smtp.auth"));
    }

    @Test
    void testAlertMailPropsEqualsHashCodeToString() {
        Map<String, String> properties = Map.of("mail.smtp.auth", "false");
        AlertMailProps props1 = new AlertMailProps("localhost", 1025, properties);
        AlertMailProps props2 = new AlertMailProps("localhost", 1025, properties);
        AlertMailProps props3 = new AlertMailProps("remotehost", 25, properties);

        assertEquals(props1, props2);
        assertEquals(props1.hashCode(), props2.hashCode());
        assertNotEquals(props1, props3);
        assertTrue(props1.toString().contains("localhost"));
        assertTrue(props1.toString().contains("1025"));
    }
}
