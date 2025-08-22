package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateStudentRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    String name,

    @NotBlank(message = "El apellido no puede estar vacío")
    String lastName,

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 8, message = "El DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números")
    String dni,

    @NotBlank (message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "\\d{10,11}" , message ="El número de teléfono debe tener entre 10 y 11 dígitos" )
    String cellphoneNumber,

    @NotNull(message = "El tipo de plan es obligatorio")
    PlanType planType,

    @NotNull (message = "Las clases por semana son obligatorias")
    @Min(value = 1, message = "Debe haber al menos 1 clase por semana")
    @Max(value = 7, message = "No puede haber más de 7 clases por semana")
    Byte classesPerWeek,

    Byte paymentDay,

    @NotNull(message = "La fecha de cumpleaños es obligatoria")
    LocalDate birthday,

    String pathologies

    )
{}

