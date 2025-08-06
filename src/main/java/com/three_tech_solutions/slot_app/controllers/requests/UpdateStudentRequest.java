package com.three_tech_solutions.slot_app.controllers.requests;

import com.three_tech_solutions.slot_app.data.enums.PlanType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateStudentRequest(
    @NotBlank(message = "Nombre es obligatorio")
    String name,

    @NotBlank(message = "Apellido es obligatorio")
    String lastName,

    @NotBlank(message = "DNI es obligatorio")
    @Size(max = 8, message = "DNI no puede tener más de 8 números")
    @Pattern(regexp = "\\d+", message = "DNI debe contener solo números")
    String dni,

    @NotBlank (message = "Teléfono es obligatorio")
    @Pattern(regexp = "\\d{9,15}" , message ="Teléfono debe tener entre 9 y 15 dígitos" )//minimo 9 digitos numéricos y maximo 15, sin espacios
    String cellphoneNumber,

    @NotNull(message = "El tipo de plan es obligatorio")
    PlanType planType,

    @NotNull
    @Min(value = 1, message = "Debe haber al menos 1 clase por semana")
    @Max(value = 7, message = "No puede haber más de 7 clases por semana")
    Byte classesPerWeek,

    Byte paymentDay,

    @NotNull(message = "Cumpleaños es obligatorio")
    LocalDate birthday,

    String pathologies

    )
{}

