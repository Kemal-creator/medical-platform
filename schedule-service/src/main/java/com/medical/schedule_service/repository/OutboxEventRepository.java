package com.medical.schedule_service.repository;

import com.medical.schedule_service.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByStatus(String status);

    boolean existsByEventTypeAndPayloadContaining(String eventType, String text);
}
