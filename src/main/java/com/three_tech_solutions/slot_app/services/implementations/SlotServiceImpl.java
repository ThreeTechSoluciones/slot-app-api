package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.ListSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SlotResponse;
import com.three_tech_solutions.slot_app.data.enums.SlotStatus;
import com.three_tech_solutions.slot_app.data.mappers.SlotMapper;
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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public ListSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek) {

        List<Slot> slots = slotRepository.findAllByUserIdAndDayOfWeekOrdered(userId, dayOfWeek);

        List<SlotResponse> responses = slots.stream()
                .map(slot -> {
                    return slotMapper.toSlotResponse(slot, calculateUsedCapacity(slot));
                })
                .toList();

        return new ListSlotsResponse(responses.size(), responses);
    }

    private int calculateUsedCapacity(Slot slot) {
        return slot.getSpecificSlots()
                .stream()
                .mapToInt(this::getUsedCapacitySpecificSlot)
                .sum();
    }

    private int getUsedCapacitySpecificSlot(SpecificSlot specificSlot) {
        if (specificSlot.getSpecificSlotDetails() == null) return 0;

        return (int) specificSlot.getSpecificSlotDetails()
                .stream()
                .filter(detail -> detail.getStudent() != null)
                .count();
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
                SlotStatus.CREATED
        );
    }

    private static LocalDate getNextDateOfDayOfWeek(CreateSlotRequest request) {
        return LocalDate.now().with(TemporalAdjusters.next(request.dayOfWeek()));
    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }
}
