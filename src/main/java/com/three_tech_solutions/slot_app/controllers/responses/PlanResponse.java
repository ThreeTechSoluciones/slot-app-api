package com.three_tech_solutions.slot_app.controllers.responses;

import java.util.UUID;

public record PlanResponse(
        UUID id,
        String name,
        double price,
        int numberOfDays
) { }
