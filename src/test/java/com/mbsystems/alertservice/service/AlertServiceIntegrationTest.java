package com.mbsystems.alertservice.service;

import com.mbsystems.alertservice.entity.Alert;
import com.mbsystems.alertservice.model.AlertingEvent;
import com.mbsystems.alertservice.repository.AlertRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@EmbeddedKafka(partitions = 1, topics = {"energy-alert"})
class AlertServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Container
    static GenericContainer<?> mailpit = new GenericContainer<>(DockerImageName.parse("axllent/mailpit:latest"))
            .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailpit::getHost);
        registry.add("spring.mail.port", () -> mailpit.getMappedPort(1025));
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @AfterEach
    void tearDown() throws Exception {
        alertRepository.deleteAll();
        clearMailpitMessages();
    }

    @Test
    void testDirectAlertServiceExecutionWithMailpitAndPostgres() throws Exception {
        AlertingEvent event = new AlertingEvent(
                1001L,
                "High power usage alert",
                75.0,
                110.5,
                "resident1001@energytracker.com"
        );

        alertService.energyUsageAlertEvent(event);

        List<Alert> alerts = alertRepository.findAll();
        assertEquals(1, alerts.size());
        Alert savedAlert = alerts.getFirst();
        assertEquals(1001L, savedAlert.userId());
        assertTrue(savedAlert.sent());
        assertNotNull(savedAlert.createdAt());
        assertNotNull(savedAlert.id());

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    String messagesJson = getMailpitMessages();
                    assertTrue(messagesJson.contains("resident1001@energytracker.com"));
                    assertTrue(messagesJson.contains("Energy Usage Alert for User 1001"));
                });
    }

    @Test
    void testKafkaTriggeredAlertServiceIntegrationWithMailpitAndPostgres() {
        AlertingEvent event = new AlertingEvent(
                2002L,
                "Peak load exceeded limit",
                120.0,
                160.0,
                "resident2002@energytracker.com"
        );

        kafkaTemplate.send("energy-alert", event);

        Awaitility.await()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    List<Alert> alerts = alertRepository.findAll();
                    assertFalse(alerts.isEmpty(), "Expected alert to be saved in PostgreSQL");
                    Alert savedAlert = alerts.stream()
                            .filter(a -> a.userId().equals(2002L))
                            .findFirst()
                            .orElse(null);
                    assertNotNull(savedAlert, "Saved alert for user 2002 should exist");
                    assertTrue(savedAlert.sent());

                    String messagesJson = getMailpitMessages();
                    assertTrue(messagesJson.contains("resident2002@energytracker.com"), "Mailpit should receive email for recipient");
                    assertTrue(messagesJson.contains("Energy Usage Alert for User 2002"), "Mailpit should have correct subject");
                });
    }

    private String getMailpitMessages() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + mailpit.getHost() + ":" + mailpit.getMappedPort(8025) + "/api/v1/messages"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private void clearMailpitMessages() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + mailpit.getHost() + ":" + mailpit.getMappedPort(8025) + "/api/v1/messages"))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
