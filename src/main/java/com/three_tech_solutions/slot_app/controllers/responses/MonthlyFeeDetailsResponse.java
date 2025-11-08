package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MonthlyFeeDetailsResponse {
    private UUID id;
    private int number;
    private LocalDateTime paymentDate;
    private double amount;
    private MonthlyFeeStatus status;
    private LocalDateTime expirationDate;
}
