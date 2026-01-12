package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpecificSlotDetailRepository extends JpaRepository<SpecificSlotDetail, UUID> {
    Optional<SpecificSlotDetail> findBySpecificSlotIdAndStudentId(UUID specificSlotId, UUID studentId);
}
