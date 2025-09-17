package com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BeginningOfMonthMonthlyFeeProcessor extends MonthlyFeeProcessor {

    private static final int BEGINNING_OF_MONTH_EXPIRATION_DATE = 10;

    @Override
    public PaymentPlanName getCurrentPlan() {
        return PaymentPlanName.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);
    }

    @Override
    public MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        return getTodayDay() <= 10 ?
                createStudentPayment(student, newPaymentNumber) :
                createPaymentWithExtraClasses(student, newPaymentNumber, extraClasses);
    }

    private static int getTodayDay() {
        return LocalDate.now().getDayOfMonth();
    }

    private MonthlyFee createPaymentWithExtraClasses(Student student, int newPaymentNumber, Byte extraClasses) {
        return createPayment(
                LocalDateTime.now(),
                student,
                newPaymentNumber,
                calculateExtraClassesAmount(student, extraClasses)
        );
    }

    private double calculateExtraClassesAmount(Student student, Byte extraClasses) {
        // TODO: Obtener precio
        /*
        validateExtraClassesOrThrowException(extraClasses);
        double pricePerClass = student.getUser().getPrices().stream()
                .filter(p -> p.getName().equals("Clase"))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se encontró precio por clase"))
                .getAmount();

        return extraClasses * pricePerClass;
         */
        return 0.0;
    }
}
