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
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ScheduleRepository scheduleRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    @Scheduled(fixedDelay = 3600000)
    public void remindIn24Hours() {
        LocalDate target = LocalDate.now().plusDays(1);
        List<Schedule> schedules = scheduleRepository.findByDate(target);
        int created = createReminders(schedules, "APPOINTMENT_REMINDER_24H");
        log.info("24h reminders: checked={}, created={}, date={}", schedules.size(), created, target);
    }

    @Transactional
    @Scheduled(fixedDelay = 3600000)
    public void remindIn72Hours() {
        LocalDate target = LocalDate.now().plusDays(3);
        List<Schedule> schedules = scheduleRepository.findByDate(target);
        int created = createReminders(schedules, "APPOINTMENT_REMINDER_72H");
        log.info("72h reminders: checked={}, created={}, date={}", schedules.size(), created, target);
    }

    @Transactional
    @Scheduled(fixedDelay = 300000)
    public void remindIn2Hours() {
        LocalDate today = LocalDate.now();
        LocalTime from = LocalTime.now().plusHours(2);
        LocalTime to = LocalTime.now().plusHours(2).plusMinutes(30);
        List<Schedule> schedules = scheduleRepository.findByDateAndTimeSlotBetween(today, from, to);
        int created = createReminders(schedules, "APPOINTMENT_REMINDER_2H");
        log.info("2h reminders: checked={}, created={}, window={}-{}", schedules.size(), created, from, to);
    }

    private int createReminders(List<Schedule> schedules, String eventType) {
        int created = 0;
        for (Schedule schedule : schedules) {
            String scheduleIdMarker = "\"scheduleId\":" + schedule.getId();
            boolean alreadyExists = outboxEventRepository
                    .existsByEventTypeAndPayloadContaining(eventType, scheduleIdMarker);

            if (alreadyExists) {
                log.debug("Reminder {} already exists for scheduleId={}, skipping", eventType, schedule.getId());
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
            event.setEventType(eventType);
            event.setPayload(payload);
            event.setStatus("PENDING");
            event.setCreatedAt(LocalDateTime.now());
            outboxEventRepository.save(event);
            created++;
        }
        return created;
    }
}
