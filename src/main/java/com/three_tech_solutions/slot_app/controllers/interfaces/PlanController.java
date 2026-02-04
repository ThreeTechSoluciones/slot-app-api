package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdatePlanRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/plans")
public interface PlanController {
    @PostMapping
    PlanResponse createPlan(@RequestBody @Valid CreatePlanRequest createPlanRequest);

    @PatchMapping("/{planId}")
    PlanResponse updatePlan(@RequestBody @Valid UpdatePlanRequest updatePlanRequest, @PathVariable UUID planId);

    @DeleteMapping("/{planId}")
    void deletePlan(@PathVariable UUID planId);

}

