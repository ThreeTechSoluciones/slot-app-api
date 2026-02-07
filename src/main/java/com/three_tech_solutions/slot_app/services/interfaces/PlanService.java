package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;

import java.util.List;
import java.util.UUID;

public interface PlanService {
    PlanResponse createPlan(CreatePlanRequest createPlanRequest);

    Plan getPlanByIdOrThrowException(UUID planId);

    List<PlanResponse> getPlansByUserAndName(User user, String planName);

    void deletePlan(UUID planId);

    PlanResponse updatePlan(UUID planId, UpdatePlanRequest updatePlanRequest);
}
