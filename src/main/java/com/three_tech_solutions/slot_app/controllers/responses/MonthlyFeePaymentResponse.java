package com.three_tech_solutions.slot_app.controllers.responses;

import java.time.LocalDate;
import java.util.UUID;

public record MonthlyFeePaymentResponse(
        UUID paymentId,
        int paymentNumber,
        double amount,
        LocalDate paymentDate,
        UUID studentId,
        String status
) {}
