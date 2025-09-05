package com.three_tech_solutions.slot_app.components.payment_processors.implementations;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component

public class SpecificDayPaymentProcessor extends PaymentProcessor {
    @Override
    public PlanType getCurrentPlan() {
        return PlanType.SPECIFIC_DAY;
    }
    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(student.getPlan().getPaymentDay());
    }

    @Override
    public Payment createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        return createStudentPayment(student, newPaymentNumber);
    }
}
