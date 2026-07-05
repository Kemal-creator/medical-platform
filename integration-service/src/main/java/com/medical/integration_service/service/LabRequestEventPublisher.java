package com.medical.integration_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabRequestEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "lab-request-created";

    public void publishLabRequestCreated(Long patientId, String testType) {
        String message = String.format(
                "{\"patientId\":%d,\"testType\":\"%s\"}",
                patientId, testType
        );
        log.info("Publishing to {}: {}", TOPIC, message);
        kafkaTemplate.send(TOPIC, message);
    }
}
