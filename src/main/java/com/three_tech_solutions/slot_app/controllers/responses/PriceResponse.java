package com.three_tech_solutions.slot_app.controllers.responses;

import java.util.UUID;

public record PriceResponse(
    UUID id,
    double amount
) {
}
