package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;

import java.time.LocalDate;
import java.util.UUID;

public record StudentMonthlyFeeResponse(
        UUID id,
        int number,
        String month,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate expirationDate,
        double amount,
        MonthlyFeeStatus status
) {}