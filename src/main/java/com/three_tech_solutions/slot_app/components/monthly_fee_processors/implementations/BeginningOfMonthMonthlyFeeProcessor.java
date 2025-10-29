package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class BeginningOfMonthMonthlyFeeProcessor extends MonthlyFeeProcessor {

    public static final int BEGINNING_OF_MONTH_EXPIRATION_DATE = 10;

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDate getNextExpirationDate(Student student) {
        return student
                .getLatestMonthlyFee()
                .map(monthlyFee ->
                        monthlyFee
                                .getExpirationDate()
                                .plusMonths(1)
                                .withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE)
                )
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "El estudiante no tiene pagos previos"));
    }

    @Override
    public LocalDate getExpirationDate(Student student) {
        return itsEndOfMonth() ?
                LocalDate.now().plusMonths(1).withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE) :
                LocalDate.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);

    }

    @Override
    public double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest) {
        return getTodayDay() <= BEGINNING_OF_MONTH_EXPIRATION_DATE ?
                    getStudentPlanPrice(student) :
                    calculateExtraClassesAmount(createStudentRequest.getClassPrice(), createStudentRequest.getExtraClasses());
    }

    /**
     * Validate if student already has the monthly fee of the month.
     * If it's end on month, processor must check the monthly fee of the next month.
     * If it's not the end of month, processor must check the monthly fee of the current month.
     */
    @Override
    public boolean studentHasTheCurrentMonthlyFee(Student student) {
        Month monthToValidate = itsEndOfMonth() ?
                LocalDate.now().plusMonths(1).getMonth() :
                LocalDate.now().getMonth();

        return student.getMonthlyFees().stream().anyMatch(monthlyFee ->
                monthlyFee.getExpirationDate().getMonth().equals(monthToValidate)
        );
    }

    /**
     * This method is because BeginningOfMonthMonthlyFeeProcessor behavior
     * depends on whether the date is the end of the month or not.
     */
    private boolean itsEndOfMonth() {
        return getTodayDay() >= 28 && getTodayDay() <= 31;
    }

    private double calculateExtraClassesAmount(Double classPrice, Byte extraClasses) {
        return classPrice * extraClasses;
    }
}
