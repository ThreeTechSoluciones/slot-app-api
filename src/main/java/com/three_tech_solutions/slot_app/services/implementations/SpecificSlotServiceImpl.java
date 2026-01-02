package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.SpecificSlotRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SpecificSlotServiceImpl implements SpecificSlotService {
    private final SpecificSlotRepository specificSlotRepository;

    @Override
    public List<SpecificSlot> getAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate) {
        return specificSlotRepository.findAllBySlot_UserAndSlotDateBetween(user, startDate, endDate);
    }

    @Override
    public SpecificSlot getSpecificSlotByIdOrThrowException(UUID specificSlotId) {
        return specificSlotRepository.findById(specificSlotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El turno solicitado no se encuentra registrado"));
    }

    @Override
    public void saveSpecificSlot(SpecificSlot specificSlot) {
        this.specificSlotRepository.save(specificSlot);
    }
}
