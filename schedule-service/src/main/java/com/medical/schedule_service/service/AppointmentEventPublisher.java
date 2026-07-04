package com.medical.schedule_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "appointment-created";

    public void publishAppointmentCreated(Long patientId, Long doctorId, String timeSlot) {
        String message = String.format(
                "{\"patientId\":%d,\"doctorId\":%d,\"timeSlot\":\"%s\"}",
                patientId, doctorId, timeSlot
        );
        kafkaTemplate.send(TOPIC, message);
    }
}
