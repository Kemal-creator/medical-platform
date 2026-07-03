package com.medical.integration_service.repository;

import com.medical.integration_service.entity.LabRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabRequestRepository extends JpaRepository<LabRequest, Long> {
}
