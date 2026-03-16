package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PaymentController;
import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;
    @Override
    public PaymentDetailsResponse getPaymentDetails(UUID paymentId) {
        return paymentService.getPaymentDetails(paymentId);
    }

}
