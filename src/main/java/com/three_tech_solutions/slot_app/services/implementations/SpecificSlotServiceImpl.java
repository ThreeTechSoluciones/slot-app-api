package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.SpecificSlotRepository;
import com.three_tech_solutions.slot_app.exceptions.custom_exceptions.StudentAlreadyRegisteredException;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotDetailService;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus.*;

@Slf4j
@Service
@AllArgsConstructor
public class SpecificSlotServiceImpl implements SpecificSlotService {

    private final SpecificSlotRepository specificSlotRepository;
    private final StudentService studentService;
    private final SpecificSlotDetailService specificSlotDetailService;

    @Override
    public List<SpecificSlot> getAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate) {
        return specificSlotRepository.findAllByUserAndSlotDateBetween(user, startDate, endDate);
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

    @Override
    public void deleteSpecificSlots(List<SpecificSlot> specificSlots) {
        specificSlotRepository.deleteAll(specificSlots);
    }

    @Override
    public void cancelSpecificSlot(UUID specificSlotId, boolean studentsMustRecoverSlot) {
        SpecificSlot specificSlot = getSpecificSlotByIdOrThrowException(specificSlotId);
        validateIfSpecificSlotIsNotAlreadyCanceled(specificSlot);
        if (studentsMustRecoverSlot) {
            specificSlot
                    .getSpecificSlotDetails()
                    .forEach(specificSlotDetail ->
                            registerAbsenceForStudent(specificSlotId, specificSlotDetail)
                    );
        }

        specificSlot.setStatus(CANCELED);
        specificSlot.getSpecificSlotDetails().clear();
        specificSlotRepository.save(specificSlot);
    }

    @Override
    public List<SpecificSlotResponse.Student> getStudentsInSpecificSlot(UUID specificSlotId, String filter) {
        return specificSlotDetailService
                .getSpecificSlotDetailsBySpecificSlot(specificSlotId, filter)
                .stream()
                .map(StudentMapper::buildStudentResponse)
                .toList();
    }

    @Override
    public void finishPastSpecificSlots(List<SpecificSlot> specificSlots) {
        List<SpecificSlot> specificSlotsToFinish = getSpecificSlotsToFinish(specificSlots);

        if (specificSlotsToFinish.isEmpty()) {
            return;
        }

        specificSlotsToFinish
                .forEach(specificSlot -> specificSlot.setStatus(FINISHED));

        specificSlotRepository.saveAll(specificSlotsToFinish);
    }

    private static void validateIfSpecificSlotIsNotAlreadyCanceled(SpecificSlot specificSlot) {
        if (specificSlot.getStatus() == CANCELED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El turno ya se encuentra cancelado");
        }
    }

    private void registerAbsenceForStudent(UUID specificSlotId, SpecificSlotDetail specificSlotDetail) {
        try {
            studentService.registerStudentAbsenceForSpecificSlot(
                    specificSlotDetail.getStudent().getId(),
                    specificSlotId
            );
        } catch (StudentAlreadyRegisteredException e) {
            log.info("Student with ID {} was already registered as absent for specific slot with ID {}",
                    specificSlotDetail.getStudent().getId(),
                    specificSlotId
            );
        }
    }

    private List<SpecificSlot> getSpecificSlotsToFinish(List<SpecificSlot> specificSlots) {

        return specificSlots.stream()
                .filter(specificSlot -> specificSlot.getStatus() == CREATED)
                .filter(SpecificSlot::hasFinished)
                .toList();
    }
}
