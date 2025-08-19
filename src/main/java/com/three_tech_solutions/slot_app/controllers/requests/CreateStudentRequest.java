package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateStudentRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 8, message = "El DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    private String dni;

    @NotBlank
    @Pattern(regexp = "\\d{10,11}") //minimo 10 digitos numéricos y maximo 11, sin espacios
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

    private LocalDate admissionDate = LocalDate.now();

    @Size(min = 1)
    private String pathologies;

    private UUID userId;
}
