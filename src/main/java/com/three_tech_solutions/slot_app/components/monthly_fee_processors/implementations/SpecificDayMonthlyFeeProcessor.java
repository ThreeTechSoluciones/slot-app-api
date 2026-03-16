package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class SpecificDayMonthlyFeeProcessor extends MonthlyFeeProcessor {

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.SPECIFIC_DAY;
    }

    @Override
    public LocalDate getNextExpirationDate(Student student) {
        return student
                .getLatestMonthlyFee()
                .map(monthlyFee ->
                        monthlyFee
                                .getExpirationDate()
                                .plusMonths(1)
                                .withDayOfMonth(getStudentPaymentDay(student))
                )
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "El estudiante no tiene pagos previos"));
    }

    @Override
    public LocalDate getExpirationDate(Student student) {
        return LocalDate.now().withDayOfMonth(
                getStudentPaymentDay(student)
        );
    }

    @Override
    public double getFirstPaymentAmount(Student student, InitialPaymentContext initialPaymentContext) {
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
