package com.medical.integration_service.service;

import com.medical.integration_service.entity.LabRequest;
import com.medical.integration_service.repository.LabRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final LabRequestRepository labRequestRepository;

    public LabRequest sendRequest(LabRequest labRequest) {
        labRequest.setStatus("PENDING");
        labRequest.setCreatedAt(LocalDateTime.now());
        return labRequestRepository.save(labRequest);
    }

    public List<LabRequest> findAll() {
        return labRequestRepository.findAll();
    }

    public LabRequest findById(Long id) {
        return labRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LabRequest not found: " + id));
    }

    public LabRequest updateResult(Long id, String result) {
        LabRequest labRequest = findById(id);
        labRequest.setResult(result);
        labRequest.setStatus("COMPLETED");
        return labRequestRepository.save(labRequest);
    }
}
