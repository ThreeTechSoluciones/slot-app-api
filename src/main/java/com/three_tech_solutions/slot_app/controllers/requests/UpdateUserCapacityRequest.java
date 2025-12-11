package com.three_tech_solutions.slot_app.controllers.requests;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateUserCapacityRequest(
        @PositiveOrZero(message = "La capacidad debe ser igual o mayor a 0.")
        byte capacity
) {
}
