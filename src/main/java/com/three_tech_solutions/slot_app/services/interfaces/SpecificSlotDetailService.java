package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;

import java.util.Optional;
import java.util.UUID;

public interface SpecificSlotDetailService {
    Optional<SpecificSlotDetail> getSpecificSlotDetailBySpecificSlotIdAndStudentId(UUID specificSlotId, UUID studentId);
}
