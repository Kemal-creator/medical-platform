package com.medical.integration_service.controller;

import com.medical.integration_service.entity.LabRequest;
import com.medical.integration_service.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-requests")
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationService integrationService;

    @PostMapping
    public LabRequest create(@RequestBody LabRequest labRequest) {
        return integrationService.sendRequest(labRequest);
    }

    @GetMapping
    public List<LabRequest> findAll() {
        return integrationService.findAll();
    }

    @GetMapping("/{id}")
    public LabRequest findById(@PathVariable Long id) {
        return integrationService.findById(id);
    }

    @PutMapping("/{id}/result")
    public LabRequest updateResult(@PathVariable Long id, @RequestParam String result) {
        return integrationService.updateResult(id, result);
    }
}
