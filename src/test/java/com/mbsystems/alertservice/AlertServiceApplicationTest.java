package com.mbsystems.alertservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlertServiceApplicationTest {

    @Test
    void testApplicationClassInstantiation() {
        AlertServiceApplication application = new AlertServiceApplication();
        assertNotNull(application);
    }
}
