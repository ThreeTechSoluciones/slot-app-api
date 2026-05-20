package com.three_tech_solutions.slot_app.controllers.responses;

import java.time.LocalDate;
import java.util.UUID;

public record PriceResponse(
    UUID id,
    LocalDate startDate,
    double amount,
    int daysUntilActive
) {
}
