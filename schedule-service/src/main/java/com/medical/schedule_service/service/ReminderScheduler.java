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
    @Scheduled(fixedDelay = 30000)
    public void scheduleReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Schedule> schedules = scheduleRepository.findByDate(tomorrow);

        int created = 0;
        for (Schedule schedule : schedules) {
            String scheduleIdMarker = "\"scheduleId\":" + schedule.getId();
            boolean alreadyExists = outboxEventRepository
                    .existsByEventTypeAndPayloadContaining("APPOINTMENT_REMINDER_24H", scheduleIdMarker);

            if (alreadyExists) {
                log.debug("Reminder already exists for scheduleId={}, skipping", schedule.getId());
                continue;
            }

            String payload = String.format(
                    "{\"scheduleId\":%d,\"doctorId\":%d,\"date\":\"%s\",\"timeSlot\":\"%s\"}",
                    schedule.getId(),
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
            created++;
        }

        log.info("Checked {} schedules for {}, created {} reminders", schedules.size(), tomorrow, created);
    }
}
