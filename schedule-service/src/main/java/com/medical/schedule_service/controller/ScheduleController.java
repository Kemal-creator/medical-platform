package com.medical.schedule_service.controller;

import com.medical.schedule_service.entity.Schedule;
import com.medical.schedule_service.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<Schedule> findAll() {
        return scheduleService.findAll();
    }

    @PostMapping
    public Schedule create(@RequestBody Schedule schedule) {
        return scheduleService.save(schedule);
    }

    @PutMapping("/{id}/book")
    public Schedule book(@PathVariable Long id) {
        return scheduleService.bookSlot(id);
    }
}
