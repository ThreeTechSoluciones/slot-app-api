package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BeginningOfMonthMonthlyFeeProcessor extends MonthlyFeeProcessor {

    public static final int BEGINNING_OF_MONTH_EXPIRATION_DATE = 10;

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);
    }

    @Override
    public double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest) {
        return getTodayDay() <= BEGINNING_OF_MONTH_EXPIRATION_DATE ?
                    getStudentPlanPrice(student) :
                    calculateExtraClassesAmount(createStudentRequest.getClassPrice(), createStudentRequest.getExtraClasses());
    }

    private static int getTodayDay() {
        return LocalDate.now().getDayOfMonth();
    }

    private double calculateExtraClassesAmount(Double classPrice, Byte extraClasses) {
        return classPrice * extraClasses;
    }
}
