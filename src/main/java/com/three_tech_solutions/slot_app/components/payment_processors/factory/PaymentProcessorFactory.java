package com.three_tech_solutions.slot_app.components.payment_processors.factory;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.DiaEspecifoPaymentProccesor;
import com.three_tech_solutions.slot_app.components.payment_processors.implementations.PrincipioDeMesPaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PaymentProcessorFactory {

    Map<PlanType, PaymentProcessor> paymentProcessors;

    public PaymentProcessorFactory(DiaEspecifoPaymentProccesor diaEspecifoPaymentProccesor, PrincipioDeMesPaymentProcessor principioDeMesPaymentProcessor) {
        this.paymentProcessors = Map.of(
                PlanType.DIA_ESPECIFICO , diaEspecifoPaymentProccesor,
                PlanType.PRINCIPIO_DE_MES, principioDeMesPaymentProcessor
        );
    }

    public PaymentProcessor getPaymentProcessor(PlanType planType){
     return paymentProcessors.get(planType);
    }
}
