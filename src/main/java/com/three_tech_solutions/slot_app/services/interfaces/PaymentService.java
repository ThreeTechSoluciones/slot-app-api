package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.util.UUID;

public interface PaymentService {

    Payment createPayment(Student student, double amount);

    PaymentDetailsResponse getPaymentDetails(UUID paymentId);
}
