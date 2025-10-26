package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SpecificDayMonthlyFeeProcessor extends MonthlyFeeProcessor {

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.SPECIFIC_DAY;
    }

    @Override
    public LocalDate getExpirationDate(Student student) {
        return LocalDate.now().withDayOfMonth(
                getStudentPaymentDay(student)
        );
    }

    @Override
    public double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest) {
        return getStudentPlanPrice(student);
    }

    @Override
    public boolean studentHasTheCurrentMonthlyFee(Student student) {
        return student.getMonthlyFees().stream().anyMatch(monthlyFee ->
                monthlyFee.getExpirationDate().getMonth().equals(LocalDate.now().getMonth())
        );
    }

    private Byte getStudentPaymentDay(Student student) {
        return student.getPaymentPlan().getPaymentDay();
    }
}
