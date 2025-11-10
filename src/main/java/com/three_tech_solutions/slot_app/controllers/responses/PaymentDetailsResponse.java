package com.three_tech_solutions.slot_app.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PaymentDetailsResponse (
    int monthlyFeeNumber,
    int paymentNumber,
    double amount,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate paymentDate
) {}

