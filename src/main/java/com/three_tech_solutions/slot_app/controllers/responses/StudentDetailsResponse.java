package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StudentDetailsResponse {
    private UUID id;
    private String name;
    private String lastName;
    private String dni;
    private String cellphoneNumber;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;
    private String pathologies;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate admissionDate;
    private String planType;
    private byte classesPerWeek;
    private Byte paymentDay;
    private List<PaymentDetailsResponse> payments;
}
