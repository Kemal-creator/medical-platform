package com.medical.schedule_service.service;

import com.medical.schedule_service.entity.Schedule;
import com.medical.schedule_service.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AppointmentEventPublisher eventPublisher;

    public Schedule save(Schedule schedule) {
        Schedule saved = scheduleRepository.save(schedule);
        eventPublisher.publishAppointmentCreated(
                null,
                saved.getDoctor().getId(),
                saved.getTimeSlot().toString()
        );
        return saved;
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule bookSlot(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found: " + id));
        schedule.setOccupied(true);
        return scheduleRepository.save(schedule);
    }
}
