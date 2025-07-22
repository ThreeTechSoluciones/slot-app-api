package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.PlanTypeResponse;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.mappers.PlanTypesMapper;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class PlanServiceImpl implements com.three_tech_solutions.slot_app.services.interfaces.PlanService {

    public List<PlanTypeResponse> getPlanTypes() {
        return PlanTypesMapper.toResponseList(List.of(PlanType.values()));
    }
}
