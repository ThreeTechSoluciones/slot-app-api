package com.three_tech_solutions.slot_app.components.payment_processors.implementations;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component

public class SpecificDayPaymentProcessor extends PaymentProcessor {

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.SPECIFIC_DAY;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        // TODO: Obtener la fecha a partir del plan del estudiante
        return LocalDateTime.now().withDayOfMonth(10);
    }

    @Override
    public MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        return createStudentPayment(student, newPaymentNumber);
    }
}
