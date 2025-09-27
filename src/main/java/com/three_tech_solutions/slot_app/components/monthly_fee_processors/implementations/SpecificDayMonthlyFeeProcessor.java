package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SpecificDayMonthlyFeeProcessor extends MonthlyFeeProcessor {

    public SpecificDayMonthlyFeeProcessor(PaymentRepository paymentRepository) {
        super(paymentRepository);
    }

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.SPECIFIC_DAY;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(
                student.getPaymentPlan().getPaymentDay()
        );
    }

    @Override
    public double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest) {
        return getStudentPlanPrice(student);
    }
}
