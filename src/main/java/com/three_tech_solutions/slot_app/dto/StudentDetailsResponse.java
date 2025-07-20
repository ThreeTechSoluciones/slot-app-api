package com.three_tech_solutions.slot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StudentDetailsResponse {
    private UUID id;
    private String name;
    private String lastName;
    private String cellphoneNumber;
    private LocalDate birthday;
    private String pathologies;
    private LocalDate admissionDate;

    private String planType;
    private byte classesPerWeek;
    private Byte paymentDay;

    private List<PaymentDetailsResponse> payments;
}
