package com.mbsystems.alertservice.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "alerts")
public record Alert(
    Long id,
    Long userId,
    LocalDateTime createdAt,
    Boolean sent
) {
    public static Alert create(Long userId) {
        return new Alert(null, userId, LocalDateTime.now(), true);
    }
}
