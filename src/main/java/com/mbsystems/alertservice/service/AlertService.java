package com.mbsystems.alertservice.service;

import com.mbsystems.alertservice.config.AlertEmail;
import com.mbsystems.alertservice.entity.Alert;
import com.mbsystems.alertservice.function.EmailMessageFunction;
import com.mbsystems.alertservice.model.AlertingEvent;
import com.mbsystems.alertservice.repository.AlertRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlertService {

    private final AlertEmail alertEmail;
    private final AlertRepository alertRepository;
    private final EmailMessageFunction emailMessageFunction;

    @KafkaListener(topics = "energy-alert", groupId = "alert-service")
    public void energyUsageAlertEvent(AlertingEvent alertingEvent) {
        this.alertEmail.sendEmail( emailMessageFunction.apply( alertingEvent ) );

        Alert alert = Alert.create(alertingEvent.userId());

        this.alertRepository.save(alert);
    }
}
