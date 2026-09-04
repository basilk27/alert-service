package com.mbsystems.alertservice;

import com.mbsystems.alertservice.config.AlertMailProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AlertServiceApplicationTests {

    @Autowired
    private AlertMailProps alertMailProps;

    @Test
    void contextLoads() {
        assertNotNull(alertMailProps);
        assertEquals("localhost", alertMailProps.host());
        assertEquals(1025, alertMailProps.port());
        assertNotNull(alertMailProps.properties());
        assertEquals("false", alertMailProps.properties().get("mail.smtp.auth"));
        assertEquals("false", alertMailProps.properties().get("mail.smtp.starttls.enable"));
    }

}
