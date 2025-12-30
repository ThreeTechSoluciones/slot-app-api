package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.data.mappers.SlotMapper;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.SlotRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SlotService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;
    private final UserService userService;
    private final SlotMapper slotMapper;

    public SlotServiceImpl(SlotRepository slotRepository, @Lazy UserService userService, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.userService = userService;
        this.slotMapper = slotMapper;
    }

    @Override
    public void createSlot(CreateSlotRequest request) {
        if(timeSlotIsAlreadyUsed(request))
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un turno que coincide con el día y horario ingresado");
        
        slotRepository.save(buildSlot(request));
    }

    @Override
    public UserSlotsResponse getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek) {
        return getSlotsByUserAndDayOfWeek(user, dayOfWeek).stream()
                .map(slot -> slotMapper.toSlotResponse(slot, calculateUsedCapacity(slot)))
                .collect(collectListAndBuildListSlotsResponse());
    }

    @Override
    public void deleteSlot(UUID slotId) {
        Slot slot = getSlotByIdOrThrowException(slotId);

        validateSlotIsActive(slot);
        validateSlotHasNoStudents(slot);
        deleteFutureSpecificSlotsPhysically(slot);
        logicallyDeleteSpecificSlots(slot);
        slot.setActive(false);
        slotRepository.save(slot);
    }

    private List<Slot> getSlotsByUserAndDayOfWeek(User user, DayOfWeek dayOfWeek) {
        return slotRepository.findAllByUserAndDayOfWeekAndActiveTrueOrderByStartTimeAsc(user, dayOfWeek);
    }
    private Collector<UserSlotResponse, Object, UserSlotsResponse> collectListAndBuildListSlotsResponse() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new UserSlotsResponse(list.size(), list)
        );
    }

    private int calculateUsedCapacity(Slot slot) {
        return slot.getStudents().size();
    }

    private Slot getSlotByIdOrThrowException(UUID slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "El turno no existe."));
    }

    private boolean timeSlotIsAlreadyUsed(CreateSlotRequest request) {
        return slotRepository.existsWithinRange(request.startTime(), request.dayOfWeek());
    }

    private Slot buildSlot(CreateSlotRequest request) {
        User user = getUserByIdOrThrowException(request.userId());
        return new Slot(
                request.dayOfWeek(),
                request.startTime(),
                request.startTime().plusMinutes(user.getUserPreferences().getSlotDurationMinutes()),
                user.getUserPreferences().getSlotCapacity(),
                user,
                createSpecificSlots(
                        request,
                        user.getUserPreferences().getSlotDurationMinutes(),
                        user.getUserPreferences().getSlotCapacity()
                )
        );
    }

    private List<SpecificSlot> createSpecificSlots(
            CreateSlotRequest request,
            long slotDurationMinutes,
            byte slotCapacity
    ) {
        List<SpecificSlot> specificSlots = new ArrayList<>();
        LocalDate date = getNextDateOfDayOfWeek(request);
        LocalDate endDate = date.plusMonths(2);

        while (date.isBefore(endDate) || date.isEqual(endDate)) {
            specificSlots.add(buildSpecificSlot(request, date, slotCapacity, slotDurationMinutes));
            date = date.plusWeeks(1);
        }

        return specificSlots;
    }

    private SpecificSlot buildSpecificSlot(
            CreateSlotRequest request,
            LocalDate startDate,
            byte slotCapacity,
            long slotDurationMinutes
    ) {
        return new SpecificSlot(
                startDate,
                slotCapacity,
                request.startTime(),
                request.startTime().plusMinutes(slotDurationMinutes),
                SpecificSlotStatus.CREATED
        );
    }

    private static LocalDate getNextDateOfDayOfWeek(CreateSlotRequest request) {
        return LocalDate.now().with(TemporalAdjusters.next(request.dayOfWeek()));
    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }

    private void validateSlotIsActive(Slot slot) {
        if (!slot.isActive()) {throw new ResponseStatusException(BAD_REQUEST, "El turno ya fue eliminado");
        }
    }

    private void validateSlotHasNoStudents(Slot slot) {
        if (!slot.getStudents().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "No se puede eliminar el turno ya que tiene alumnos asociados");
        }
    }

    private void deleteFutureSpecificSlotsPhysically(Slot slot) {
        LocalDate today = LocalDate.now();

        slot.getSpecificSlots().removeIf(specificSlot ->
                specificSlot.getSlotDate().isAfter(today) ||
                        (specificSlot.getSlotDate().isEqual(today) && specificSlot.getStartTime().isAfter(LocalTime.now())));
    }

    private void logicallyDeleteSpecificSlots(Slot slot) {
        slot.getSpecificSlots()
                .forEach(specificSlot -> specificSlot.setStatus(SpecificSlotStatus.DELETED));
    }
}
