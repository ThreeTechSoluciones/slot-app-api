package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class BeginningOfMonthMonthlyFeeProcessor extends MonthlyFeeProcessor {

    public static final int BEGINNING_OF_MONTH_EXPIRATION_DATE = 10;
    public static final int DAYS_DIFFERENCE_TO_CREATE_MONTHLY_FEE = 2;

    @Override
    public boolean satisfiesTheConditionsOfThePaymentDate(Student student) {
        return getDifferenceBetweenMaxDayOfMonthAndTodayDay() <= DAYS_DIFFERENCE_TO_CREATE_MONTHLY_FEE;
    }

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDate getExpirationDate(Student student) {
        return LocalDate.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);
    }

    @Override
    public double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest) {
        return getTodayDay() <= BEGINNING_OF_MONTH_EXPIRATION_DATE ?
                    getStudentPlanPrice(student) :
                    calculateExtraClassesAmount(createStudentRequest.getClassPrice(), createStudentRequest.getExtraClasses());
    }

    @Override
    public boolean studentDoesNotHaveCurrentMonthlyFee(Student student) {
//        if (getTodayDay() >= 28 && getTodayDay() <= 31) {
//            return student.getMonthlyFees().stream().anyMatch(monthlyFee ->
//                    monthlyFee.getExpirationDate().withMonth(LocalDate.now().getMonthValue())
//            );
//        }

        return false;
    }

    private double calculateExtraClassesAmount(Double classPrice, Byte extraClasses) {
        return classPrice * extraClasses;
    }

    private int getDifferenceBetweenMaxDayOfMonthAndTodayDay() {
        return getMaxDayOfMonth() - getTodayDay();
    }

    private int getMaxDayOfMonth() {
        return YearMonth.now().atEndOfMonth().getDayOfMonth();
    }

}
