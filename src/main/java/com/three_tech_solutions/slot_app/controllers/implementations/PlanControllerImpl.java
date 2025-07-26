package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.PlanController;
import com.three_tech_solutions.slot_app.controllers.responses.PlanTypeResponse;
import com.three_tech_solutions.slot_app.services.implementations.PlanServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class PlanControllerImpl implements PlanController {

    private final PlanServiceImpl planService;

    @Override
    public List<PlanTypeResponse> getPlanTypes(){
        return planService.getPlanTypes();
    }
}
