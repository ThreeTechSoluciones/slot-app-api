package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;

import java.time.LocalDate;
import java.util.UUID;

public record StudentMonthlyFeeResponse(
        UUID id,
        int number,
        String month,
        LocalDate expirationDate,
        double amount,
        MonthlyFeeStatus status
) {}