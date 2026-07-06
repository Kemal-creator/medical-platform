package com.medical.schedule_service.service;

import com.medical.schedule_service.entity.OutboxEvent;
import com.medical.schedule_service.entity.Schedule;
import com.medical.schedule_service.repository.OutboxEventRepository;
import com.medical.schedule_service.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ScheduleRepository scheduleRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    @Scheduled(fixedDelay = 3600000)
    public void scheduleReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Schedule> schedules = scheduleRepository.findByDate(tomorrow);

        for (Schedule schedule : schedules) {
            String payload = String.format(
                    "{\"doctorId\":%d,\"date\":\"%s\",\"timeSlot\":\"%s\"}",
                    schedule.getDoctor().getId(),
                    schedule.getDate(),
                    schedule.getTimeSlot()
            );

            OutboxEvent event = new OutboxEvent();
            event.setEventType("APPOINTMENT_REMINDER_24H");
            event.setPayload(payload);
            event.setStatus("PENDING");
            event.setCreatedAt(LocalDateTime.now());
            outboxEventRepository.save(event);
        }

        log.info("Scheduled {} reminders for {}", schedules.size(), tomorrow);
    }
}
