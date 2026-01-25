package com.three_tech_solutions.slot_app.controllers.responses;

public record StudentPaymentsMetricResponse(
        int onTimeCount,
        int outstandingCount,
        int payedCount
) {
}
