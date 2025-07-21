package com.three_tech_solutions.slot_app.controllers.implementations;
import com.three_tech_solutions.slot_app.controllers.interfaces.PlanController;
import com.three_tech_solutions.slot_app.controllers.responses.PlanTypesResponse;
import com.three_tech_solutions.slot_app.services.implementations.PlanService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class PlanControllerImpl implements PlanController {

    private final PlanService planService;

    public PlanControllerImpl(PlanService planService) {
        this.planService = planService;
    }

    @Override
    public List<PlanTypesResponse> getPlanTypes (){
        return planService.getPlanTypes();
    }
}
