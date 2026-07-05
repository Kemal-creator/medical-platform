package com.medical.integration_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabResultConsumer {

    private final IntegrationService integrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "lab-result-ready", groupId = "integration-group")
    public void consume(String message) {
        log.info("Received lab result: {}", message);
        try {
            JsonNode json = objectMapper.readTree(message);
            Long labRequestId = json.get("labRequestId").asLong();
            String result = json.get("result").asText();
            integrationService.updateResult(labRequestId, result);
        } catch (Exception e) {
            log.error("Failed to process lab result message: {}", message, e);
        }
    }
}
