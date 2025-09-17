package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;

import java.time.LocalDate;
import java.util.UUID;

public record StudentDetailsResponse (
     UUID id,
     String name,
     String lastName,
     String dni,
     String cellphoneNumber,
     @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate birthday,
     String pathologies,
     @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate admissionDate,
     String paymentPlan,
     String plan,
     byte paymentDay,
     boolean status,
     StudentSituation situation
) {}
