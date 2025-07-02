package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.PlanType;

import java.util.Optional;
import java.util.UUID;

public interface PlanTypeService {
    Optional<PlanType> getById(UUID id);
}
