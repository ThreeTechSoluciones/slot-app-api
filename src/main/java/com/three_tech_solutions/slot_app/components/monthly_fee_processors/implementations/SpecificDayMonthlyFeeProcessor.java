package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component

public class SpecificDayMonthlyFeeProcessor extends MonthlyFeeProcessor {

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.SPECIFIC_DAY;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(
                student.getPlanType().getPaymentDay()
        );
    }

    @Override
    public MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        return createStudentPayment(student, newPaymentNumber);
    }
}
