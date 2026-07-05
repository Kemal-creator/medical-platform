package com.medical.patient_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Email(message = "Некорректный email")
    private String email;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate dateOfBirth;

    private String phone;
}
