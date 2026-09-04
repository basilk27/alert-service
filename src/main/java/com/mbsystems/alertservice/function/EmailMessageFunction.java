package com.mbsystems.alertservice.function;

import com.mbsystems.alertservice.model.AlertingEvent;
import com.mbsystems.alertservice.model.EmailSubjects;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmailMessageFunction implements Function<AlertingEvent, EmailSubjects> {

    private static final String SUBJECT_TEMPLATE = "Energy Usage Alert for User ";
    private static final String MESSAGE_PART_1 = "Alert: ";
    private static final String MESSAGE_PART_2 = "\nThreshold: ";
    private static final String MESSAGE_PART_3 = "\nEnergy Consumed:";

    @Override
    public EmailSubjects apply(AlertingEvent alertingEvent) {
        return new EmailSubjects(
                alertingEvent.email(),
                SUBJECT_TEMPLATE + alertingEvent.userId(),
                MESSAGE_PART_1 + alertingEvent.message() +
                        MESSAGE_PART_2 + alertingEvent.threshold() +
                        MESSAGE_PART_3 + alertingEvent.energyConsumed(),
                alertingEvent.userId()
        );
    }
}
