package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypeResponse;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;

import java.util.List;

public class PlanTypesMapper {

    public static PlanTypeResponse toResponse(PaymentPlanName paymentPlanName) {

        return new PlanTypeResponse(paymentPlanName.getName());
    }

    public static List<PlanTypeResponse> toResponseList(List<PaymentPlanName> paymentPlanNames) {
        return  paymentPlanNames
                .stream()
                .map(PlanTypesMapper::toResponse)
                .toList();
    }
}
