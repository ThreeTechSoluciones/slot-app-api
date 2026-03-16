package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PlanController;
import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@AllArgsConstructor
public class PlanControllerImpl implements PlanController {

    private final PlanService planService;

    @Override
    public PlanResponse createPlan(CreatePlanRequest createPlanRequest) {
        return planService.createPlan(createPlanRequest);
    }

    @Override
    public PlanResponse updatePlan(UpdatePlanRequest updatePlanRequest, UUID planId) {
        return planService.updatePlan(planId, updatePlanRequest);
    }

    @Override
    public void deletePlan(UUID planId) {
        planService.deletePlan(planId);
    }

}
