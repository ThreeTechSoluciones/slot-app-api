package com.three_tech_solutions.slot_app.components.payment_processors.implementations;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class BeginningOfMonthPaymentProcessor extends PaymentProcessor {

    private final int BEGINNING_OF_MONTH_EXPIRATION_DATE=10;

    @Override
    public PlanType getCurrentPlan() {
        return PlanType.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);
    }
}
