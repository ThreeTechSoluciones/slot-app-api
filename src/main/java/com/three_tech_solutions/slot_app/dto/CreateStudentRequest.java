package com.three_tech_solutions.slot_app.dto;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateStudentRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d{9,15}") //minimo 9 digitos numéricos y maximo 15, sin espacios
    private String cellphoneNumber;

    @NotNull
    private PlanType planType;

    @NotNull
    @Min(1)
    @Max(7)
    private byte classesPerWeek;

    private Byte extraClasses;

    private Byte paymentDay;

    @NotNull
    private LocalDate birthday;

    private LocalDate admissionDate = LocalDate.now();;

    @NotBlank
    private String pathologies;

    private UUID userId;
}
