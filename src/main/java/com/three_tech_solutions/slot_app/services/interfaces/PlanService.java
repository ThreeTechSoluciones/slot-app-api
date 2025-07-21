package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypesResponse;

import java.util.List;

public interface PlanService {
    List <PlanTypesResponse> getPlanTypes ();
}
