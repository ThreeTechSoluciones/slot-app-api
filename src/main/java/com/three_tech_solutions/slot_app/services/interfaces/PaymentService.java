package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;

public interface PaymentService {

    Payment createPayment(Student student, double amount);
}
