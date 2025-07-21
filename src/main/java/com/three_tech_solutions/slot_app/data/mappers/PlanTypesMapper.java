package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypesResponse;
import com.three_tech_solutions.slot_app.data.enums.PlanType;

import java.util.Arrays;
import java.util.List;

public class PlanTypesMapper {

    public static PlanTypesResponse toResponse(PlanType planType) {
        return new PlanTypesResponse(planType.getName());
    }

    public static List<PlanTypesResponse> toResponseList() {
        return Arrays.stream(PlanType.values())
                .map(PlanTypesMapper::toResponse)
                .toList();
    }
}
