package com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations.BeginningOfMonthMonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations.SpecificDayMonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MonthlyFeeProcessorFactory {

    private final Map<PaymentPlanName, MonthlyFeeProcessor> paymentProcessors;

    public MonthlyFeeProcessorFactory(SpecificDayMonthlyFeeProcessor specificDayPaymentProccesor, BeginningOfMonthMonthlyFeeProcessor beginningOfMonthPaymentProcessor) {
        this.paymentProcessors = Map.of(
                PaymentPlanName.SPECIFIC_DAY , specificDayPaymentProccesor,
                PaymentPlanName.BEGINNING_OF_MONTH, beginningOfMonthPaymentProcessor
        );
    }

    public MonthlyFeeProcessor getPaymentProcessor(PaymentPlanName paymentPlanName){
     return paymentProcessors.get(paymentPlanName);
    }
}
