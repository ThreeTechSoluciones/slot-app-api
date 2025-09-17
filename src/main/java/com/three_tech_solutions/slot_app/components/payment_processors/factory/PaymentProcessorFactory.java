package com.three_tech_solutions.slot_app.components.payment_processors.factory;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.BeginningOfMonthPaymentProcessor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.SpecificDayPaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentPlanName, PaymentProcessor> paymentProcessors;

    public PaymentProcessorFactory(SpecificDayPaymentProcessor specificDayPaymentProccesor, BeginningOfMonthPaymentProcessor beginningOfMonthPaymentProcessor) {
        this.paymentProcessors = Map.of(
                PaymentPlanName.SPECIFIC_DAY , specificDayPaymentProccesor,
                PaymentPlanName.BEGINNING_OF_MONTH, beginningOfMonthPaymentProcessor
        );
    }

    public PaymentProcessor getPaymentProcessor(PaymentPlanName paymentPlanName){
     return paymentProcessors.get(paymentPlanName);
    }
}
