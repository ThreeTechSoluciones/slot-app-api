package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.data.models.Payment;

public class PaymentMapper {
    public static PaymentDetailsResponse toPaymentDetailsResponse(Payment payment, int monthlyFeeNumber) {
        return new PaymentDetailsResponse(
                monthlyFeeNumber,
                payment.getNumber(),
                payment.getAmount(),
                payment.getPaymentDate()
        );
    }
}