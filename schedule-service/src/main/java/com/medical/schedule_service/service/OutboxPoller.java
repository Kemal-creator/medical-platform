package com.medical.schedule_service.service;

import com.medical.schedule_service.entity.OutboxEvent;
import com.medical.schedule_service.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void pollAndPublish() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatus("PENDING");
        for (OutboxEvent event : pendingEvents) {
            kafkaTemplate.send("appointment-created", event.getPayload());
            event.setStatus("SENT");
            outboxEventRepository.save(event);
            log.info("Published outbox event id={} type={}", event.getId(), event.getEventType());
        }
    }
}
