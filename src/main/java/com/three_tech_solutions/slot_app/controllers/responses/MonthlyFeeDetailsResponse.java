package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public record MonthlyFeeDetailsResponse(
    UUID id,
    int number,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime paymentDate,
    double amount,
    MonthlyFeeStatus status,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime expirationDate
){}
