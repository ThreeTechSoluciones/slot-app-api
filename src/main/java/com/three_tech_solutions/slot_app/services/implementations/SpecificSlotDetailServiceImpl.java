package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.SpecificSlotDetailRepository;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotDetailStatus;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SpecificSlotDetailServiceImpl implements SpecificSlotDetailService {

    private final SpecificSlotDetailRepository specificSlotDetailRepository;

    @Override
    public Optional<SpecificSlotDetail> getSpecificSlotDetailBySpecificSlotIdAndStudentId(UUID specificSlotId, UUID studentId) {
        return specificSlotDetailRepository.findBySpecificSlotIdAndStudentId(specificSlotId, studentId);
    }

    public void registerAbsence(SpecificSlotDetail specificSlotDetail) {
        specificSlotDetail.setStatus(SpecificSlotDetailStatus.ABSENCE);
        specificSlotDetailRepository.save(specificSlotDetail);
    }
}
