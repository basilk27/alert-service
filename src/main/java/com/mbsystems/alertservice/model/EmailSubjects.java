package com.mbsystems.alertservice.model;

public record EmailSubjects(
    String to,
    String subject,
    String body,
    Long userId
) {
}
