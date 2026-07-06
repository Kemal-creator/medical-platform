package com.medical.schedule_service.service;

import com.medical.schedule_service.entity.OutboxEvent;
import com.medical.schedule_service.entity.Schedule;
import com.medical.schedule_service.repository.OutboxEventRepository;
import com.medical.schedule_service.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    @CacheEvict(value = "schedules", allEntries = true)
    public Schedule save(Schedule schedule) {
        Schedule saved = scheduleRepository.save(schedule);

        String payload = String.format(
                "{\"scheduleId\":%d,\"doctorId\":%d,\"date\":\"%s\",\"timeSlot\":\"%s\"}",
                saved.getId(),
                saved.getDoctor().getId(),
                saved.getDate(),
                saved.getTimeSlot()
        );

        OutboxEvent event = new OutboxEvent();
        event.setEventType("APPOINTMENT_CREATED");
        event.setPayload(payload);
        event.setStatus("PENDING");
        event.setCreatedAt(LocalDateTime.now());
        outboxEventRepository.save(event);

        return saved;
    }

    @Cacheable(value = "schedules")
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "schedules", allEntries = true)
    public Schedule bookSlot(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found: " + id));
        schedule.setOccupied(true);
        return scheduleRepository.save(schedule);
    }
}
