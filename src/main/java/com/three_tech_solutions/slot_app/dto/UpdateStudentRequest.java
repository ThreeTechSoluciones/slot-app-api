package com.three_tech_solutions.slot_app.dto;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
public class UpdateStudentRequest {

    private String name;

    private String lastName;

    private String dni;

    private String cellphoneNumber;

    private PlanType planType;

    private byte classesPerWeek;

    private Byte extraClasses;

    private Byte paymentDay;

    private LocalDate birthday;

    private LocalDate admissionDate = LocalDate.now();

    private String pathologies;

}
