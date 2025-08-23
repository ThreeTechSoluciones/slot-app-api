package com.three_tech_solutions.slot_app.components.payment_processors.factory;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.DiaEspecifoPaymentProccesor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.PrincipioDeMesPaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PlanType;

import java.util.Map;

public class PaymentProcessorFactory {

    Map<PlanType, PaymentProcessor> paymentProcessors = Map.of(
            PlanType.DIA_ESPECIFICO, new DiaEspecifoPaymentProccesor(),
            PlanType.PRINCIPIO_DE_MES, new PrincipioDeMesPaymentProcessor()
    );

    public PaymentProcessor getPaymentProcessor(PlanType planType){
     return paymentProcessors.get(planType);
    }
}
