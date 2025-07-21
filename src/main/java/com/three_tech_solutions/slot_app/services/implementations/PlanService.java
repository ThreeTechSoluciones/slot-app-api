package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypesResponse;
import com.three_tech_solutions.slot_app.data.mappers.PlanTypesMapper;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class PlanService implements com.three_tech_solutions.slot_app.services.interfaces.PlanService {

    public List<PlanTypesResponse> getPlanTypes() {
        return PlanTypesMapper.toResponseList();
    }
}
