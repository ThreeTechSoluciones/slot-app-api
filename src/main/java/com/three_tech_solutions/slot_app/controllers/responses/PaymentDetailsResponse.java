package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentDetailsResponse {
    private UUID id;
    private int number;
    private LocalDateTime paymentDate;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime expirationDate;
}
