package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping ("/payments")
public interface PaymentController {

    @GetMapping("/{paymentId}")
    PaymentDetailsResponse getPaymentDetails(@PathVariable UUID paymentId);
}
