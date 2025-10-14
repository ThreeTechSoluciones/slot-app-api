package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;

import java.time.LocalDate;

public record StudentMonthlyFeeResponseDto(
        int number,
        String month,
        LocalDate expirationDate,
        double amount,
        MonthlyFeeStatus status
) {}