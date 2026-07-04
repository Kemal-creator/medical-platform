package com.medical.notification_service.service;

import com.medical.notification_service.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "appointment-created", groupId = "notification-group")
    public void consume(String message) {
        log.info("Received Kafka message: {}", message);

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType("PUSH");
        notification.setStatus("PENDING");

        notificationService.save(notification);
    }
}
