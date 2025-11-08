package com.three_tech_solutions.slot_app.controllers.responses;

import java.time.LocalDate;

public record PaymentDetailsResponse (
    int monthlyFeeNumber,
    int paymentNumber,
    double amount,
    LocalDate paymentDate
) {}

