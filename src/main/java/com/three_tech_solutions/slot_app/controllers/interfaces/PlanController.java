package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypesResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/plan-types")
public interface PlanController {
    @GetMapping
    List<PlanTypesResponse> getPlanTypes ();
}
