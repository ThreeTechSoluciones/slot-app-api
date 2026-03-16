package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePlanRequest(
        @NotBlank(message = "Debe ingresar un nombre para el plan")
        String name,
        @NotNull(message = "Debe ingresar la cantidad de días del plan")
        @Min(value = 1, message = "La cantidad de días debe ser al menos 1")
        @Max(value = 7, message = "La cantidad de días no puede ser mayor a 7")
        Byte numberOfDays,
        @NotNull(message = "Debe ingresar el precio del plan")
        @Min(value = 0, message = "El precio del plan no puede ser menor a cero")
        Double amount,
        @NotNull(message = "Debe ingresar el ID del usuario asociado al plan")
        UUID userId
) {
}
