package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.SpecificSlotRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class SpecificSlotServiceImpl implements SpecificSlotService {
    private final SpecificSlotRepository specificSlotRepository;

    @Override
    public List<SpecificSlot> getAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate) {
        return specificSlotRepository.findAllBySlot_UserAndSlotDateBetween(user, startDate, endDate);
    }
}
