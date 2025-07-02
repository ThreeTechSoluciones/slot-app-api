package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.repositories.PlanRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    //constructor
    public PlanServiceImpl(PlanRepository planRepository) {

        this.planRepository = planRepository;
    }
    // Guardar un plan en la base
    @Override
    public void save(Plan plan) {
        planRepository.save(plan);
    }
}
