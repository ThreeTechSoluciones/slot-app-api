package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PlanService {
    PlanResponse createPlan(CreatePlanRequest createPlanRequest);

    Plan getPlanByIdOrThrowException(UUID planId);

    Page<PlanResponse> getPlansByUserAndName(User user, String planName, Pageable pageable);

    void deletePlan(UUID planId);

    PlanResponse updatePlan(UUID planId, UpdatePlanRequest updatePlanRequest);
}
