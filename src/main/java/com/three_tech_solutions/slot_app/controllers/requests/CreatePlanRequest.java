package com.three_tech_solutions.slot_app.controllers.requests;

import java.util.UUID;

public record CreatePlanRequest(
        String name,
        Byte numberOfDays,
        double amount,
        UUID userId
) {
}
