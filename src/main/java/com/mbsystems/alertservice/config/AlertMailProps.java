package com.mbsystems.alertservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.mail")
public record AlertMailProps(
        String host,
        int port,
        Map<String, String> properties
) {
}
