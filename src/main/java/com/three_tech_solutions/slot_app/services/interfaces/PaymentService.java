package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.components.payment_processors.factory.PaymentProcessorFactory;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;

public interface PaymentService {

    void createStudentsPayment();
    Payment createInitialPayment(Student student, Byte extraClasses);
}
