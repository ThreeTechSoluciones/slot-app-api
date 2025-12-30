package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PlanController;
import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePriceRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.services.implementations.PlanServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@AllArgsConstructor
public class PlanControllerImpl implements PlanController {

    private final PlanServiceImpl planService;

    @Override
    public PlanResponse createPlan(CreatePlanRequest createPlanRequest) {
        return planService.createPlan(createPlanRequest);
    }

    @Override
    public PlanResponse updatePrice(UpdatePriceRequest updatePriceRequest, UUID planId) {
        return planService.updatePrice(planId, updatePriceRequest);
    }

    @Override
    public void deletePlan(UUID planId) {
        planService.deletePlan(planId);
    }

}
