package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.data.models.Plan;

import java.util.UUID;

public interface PlanService {
    PlanResponse createPlan(CreatePlanRequest createPlanRequest);

    Plan getPlanByIdOrThrowException(UUID planId);

    PlanResponse updatePrice(UUID planId, UpdatePriceRequest updatePriceRequest);
}
