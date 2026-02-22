package com.three_tech_solutions.slot_app.controllers.responses;

public record StudentsSummaryMetricsResponse(
        int activeStudentsCount,
        int activeStudentsWithDebtCount,
        int inactiveStudentsWithDebtCount
) {
}
