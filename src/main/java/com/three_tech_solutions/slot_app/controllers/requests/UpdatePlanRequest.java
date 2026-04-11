package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdatePlanRequest(
        @NotBlank(message = "Debe ingresar un nombre para el plan")
        String name,
        @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
        Double amount,
        @FutureOrPresent(message = "Se debe ingresar una fecha igual o superior a la actual.")
        LocalDate startDate
) {
}
