package com.medical.schedule_service.repository;

import com.medical.schedule_service.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDate(LocalDate date);

    List<Schedule> findByDateAndTimeSlotBetween(LocalDate date, LocalTime from, LocalTime to);
}
