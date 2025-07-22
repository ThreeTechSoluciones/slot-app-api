package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypeResponse;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import java.util.List;

public class PlanTypesMapper {

    public static PlanTypeResponse toResponse(PlanType planType) {

        return new PlanTypeResponse(planType.getName());
    }

    public static List<PlanTypeResponse> toResponseList(List<PlanType> planTypes) {
        return  planTypes
                .stream()
                .map(PlanTypesMapper::toResponse)
                .toList();
    }
}
