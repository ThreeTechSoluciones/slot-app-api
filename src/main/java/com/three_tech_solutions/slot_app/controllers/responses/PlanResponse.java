package com.three_tech_solutions.slot_app.controllers.responses;

import java.util.List;
import java.util.UUID;

public record PlanResponse(
        UUID id,
        String name,
        double currentPrice,
        PriceResponse nextPrice,
        List<PriceResponse> futurePrices,
        int numberOfDays,
        int totalFuturePrices
) { }
