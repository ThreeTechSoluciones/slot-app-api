package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record UpdateUserCapacityRequest(
        @Min(value = 1, message = "La capacidad debe ser mayor a 0.")
        @Max(value = 127, message = "La capacidad ingresada supera el límite máximo permitido.")
        int capacity
) {
}
