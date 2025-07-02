package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanTypeRepository extends JpaRepository<PlanType, UUID> {
}
