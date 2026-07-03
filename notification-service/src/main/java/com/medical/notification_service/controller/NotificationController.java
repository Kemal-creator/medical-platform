package com.medical.notification_service.controller;

import com.medical.notification_service.entity.Notification;
import com.medical.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Notification create(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @GetMapping
    public List<Notification> findAll() {
        return notificationService.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<Notification> findByPatientId(@PathVariable Long patientId) {
        return notificationService.findByPatientId(patientId);
    }
}
