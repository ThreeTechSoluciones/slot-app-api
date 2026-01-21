package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsByDayResponse;
import com.three_tech_solutions.slot_app.data.models.*;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.mappers.SlotMapper;
import com.three_tech_solutions.slot_app.data.repositories.SlotRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SlotService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final SlotMapper slotMapper;


    public SlotServiceImpl(SlotRepository slotRepository, @Lazy UserService userService, StudentService studentService, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.userService = userService;
        this.slotMapper = slotMapper;
        this.studentService = studentService;
    }

    @Override
    public void createSlot(CreateSlotRequest request) {
        User user = getUserByIdOrThrowException(request.userId());
        validateNoActiveConflicts(request.dayOfWeek(), request.startTime(), null);
        Optional<Slot> inactiveSlot = findInactiveExactSlot(user, request.dayOfWeek(), request.startTime());
        if (inactiveSlot.isPresent()) {
            recoverSlot(inactiveSlot.get(), user);
        } else {
            Slot newSlot = buildSlot(request, user);
            slotRepository.save(newSlot);
        }
    }

    @Override
    public List<UserSlotsByDayResponse> getSlotsByDayOfWeek(User user, DayOfWeek dayOfWeek) {
        List<Slot> slots = getSlots(user, dayOfWeek);
        return groupSlotsByDay(slots);
    }

    @Override
    public void addStudentToSlot(UUID slotId, UUID studentId) {
        Student student = getStudent(studentId);
        registerStudentInSlot(slotId, student);
    }

    @Override
    public void addStudentToSlot(UUID slotId, Student student) {
        registerStudentInSlot(slotId, student);
    }

    @Override
    public UserSlotResponse updateSlot(UUID slotId, UpdateSlotRequest request) {
        Slot oldSlot = getSlotByIdOrThrowException(slotId);
        User user = oldSlot.getUser();

        validateStartTimeIsDifferent(oldSlot, request);
        validateNoActiveConflicts(oldSlot.getDayOfWeek(), request.startTime(), slotId);
        Optional<Slot> inactiveSlot = findInactiveExactSlot(user, oldSlot.getDayOfWeek(), request.startTime());
        Slot finalSlot;

        if (inactiveSlot.isPresent()) {
            Slot recoveredSlot = inactiveSlot.get();
            transferStudents(oldSlot, recoveredSlot);
            deactivateSlot(oldSlot);
            finalSlot = recoverSlot(recoveredSlot, user);
        } else {
            updateSlotInPlace(oldSlot, request, user);
            finalSlot = slotRepository.save(oldSlot);
        }

        return slotMapper.toSlotResponse(finalSlot, calculateUsedCapacity(finalSlot));
    }

    @Override
    @Transactional
    public void deleteSlot(UUID slotId) {
        Slot slot = getSlotByIdOrThrowException(slotId);

        validateSlotIsActive(slot);
        validateSlotHasNoStudents(slot);
        deleteFutureSpecificSlotsPhysically(slot);
        logicallyDeleteSpecificSlots(slot);
        slot.setActive(false);
        slotRepository.save(slot);
    }

    @Override
    public List<StudentSlotResponse> getSlotsByStudent(Student student) {
        return slotRepository.findAllByStudentOrderByDayAndTime(student)
                .stream()
                .map(slotMapper::toStudentSlotResponse)
                .toList();
    }

    private List<Slot> getSlots(User user, DayOfWeek dayOfWeek) {
        return slotRepository.findAllByUserAndOptionalDayOfWeek(user, dayOfWeek);
    }

    private List<UserSlotsByDayResponse> groupSlotsByDay(List<Slot> slots) {
        return slots
                .stream()
                .collect(Collectors.groupingBy(Slot::getDayOfWeek))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(this::toUserSlotsByDayResponse)
                .toList();
    }

    private UserSlotsByDayResponse toUserSlotsByDayResponse(
            Map.Entry<DayOfWeek, List<Slot>> entry
    ) {
        List<UserSlotResponse> slotResponses = entry.getValue().stream()
                .map(slot -> slotMapper.toSlotResponse(slot, calculateUsedCapacity(slot)))
                .toList();

        return new UserSlotsByDayResponse(
                entry.getKey(),
                slotResponses.size(),
                slotResponses
        );
    }

    private int calculateUsedCapacity(Slot slot) {
        return slot.getStudents().size();
    }

    private Student getStudent(UUID studentId) {
        return studentService.getStudentByIdOrThrowExcepion(studentId);
    }

    private static boolean startDateIsTodayOrAfter(SpecificSlot specificSlot) {
        return specificSlot.getSlotDate().isEqual(LocalDate.now()) || specificSlot.getSlotDate().isAfter(LocalDate.now());
    }

    private Slot getSlotByIdOrThrowException(UUID slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "No se encontró el turno"));
    }

    private Slot buildSlot(CreateSlotRequest request, User user) {
        Slot slot = new Slot(
                request.dayOfWeek(),
                request.startTime(),
                calculateEndTime(user, request.startTime()),
                user.getUserPreferences().getSlotCapacity(),
                user,
                new ArrayList<>()
        );
        LocalDate firstDate = getNextDateOfDayOfWeek(request.dayOfWeek());
        slot.setSpecificSlots(
                createSpecificSlots(
                        slot,
                        firstDate,
                        user.getUserPreferences().getSlotDurationMinutes()
                )
        );

        return slot;
    }

    private List<SpecificSlot> createSpecificSlotsFromToday(
            Slot slot,
            User user
    ) {
        LocalDate firstDate = getNextDateOfDayOfWeek(slot.getDayOfWeek());
        long duration = user.getUserPreferences().getSlotDurationMinutes();

        return createSpecificSlots(slot, firstDate, duration);
    }

    private List<SpecificSlot> createSpecificSlots(
            Slot slot,
            LocalDate date,
            long slotDurationMinutes
    ) {
        List<SpecificSlot> specificSlots = new ArrayList<>();
        LocalDate endDate = date.plusMonths(2);

        while (date.isBefore(endDate) || date.isEqual(endDate)) {
            specificSlots.add(buildSpecificSlot(slot, date, slotDurationMinutes));
            date = date.plusWeeks(1);
        }

        return specificSlots;
    }

    private void deactivateSlot(Slot slot) {
        deleteFutureSpecificSlotsPhysically(slot);
        slot.setActive(false);
        slotRepository.save(slot);
    }

    private SpecificSlot buildSpecificSlot(
            Slot slot,
            LocalDate startDate,
            long slotDurationMinutes
    ) {
        return new SpecificSlot(
                startDate,
                slot.getCapacity(),
                slot.getStartTime(),
                slot.getStartTime().plusMinutes(slotDurationMinutes),
                SpecificSlotStatus.CREATED
        );
    }

    private static LocalDate getNextDateOfDayOfWeek(DayOfWeek dayOfWeek) {
        return LocalDate.now().with(TemporalAdjusters.next(dayOfWeek));
    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }

    private void validateNoActiveConflicts(DayOfWeek dayOfWeek, LocalTime startTime, UUID excludedSlotId) {
        if (slotRepository.existsWithinRange(startTime, dayOfWeek, excludedSlotId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un turno que coincide con el día y el rango de horario ingresado");
        }
    }

    private Optional<Slot> findInactiveExactSlot(User user, DayOfWeek dayOfWeek, LocalTime startTime) {
        return slotRepository.findAllByUserAndOptionalDayOfWeek(user, dayOfWeek)
                .stream()
                .filter(slot ->
                        !slot.isActive()
                                && slot.getStartTime().equals(startTime)
                )
                .findFirst();
    }

    private Slot recoverSlot(Slot slot, User user) {
        slot.setActive(true);

        deleteFutureSpecificSlotsPhysically(slot);

        slot.getSpecificSlots().addAll(
                createSpecificSlotsFromToday(slot, user)
        );

        return slotRepository.save(slot);
    }

    private void transferStudents(Slot from, Slot to) {
        to.getStudents().addAll(from.getStudents());
    }

    private void updateSlotInPlace(
            Slot slot,
            UpdateSlotRequest request,
            User user
    ) {
        slot.setStartTime(request.startTime());
        slot.setEndTime(calculateEndTime(user, request.startTime()));

        slot.getSpecificSlots().clear();
        slot.getSpecificSlots().addAll(
                createSpecificSlotsFromToday(slot, user)
        );
    }


    private LocalTime calculateEndTime(User user, LocalTime startTime) {
        return startTime.plusMinutes(user.getUserPreferences().getSlotDurationMinutes());
    }

    private void validateStartTimeIsDifferent(Slot slot, UpdateSlotRequest updateSlotRequest) {
        if (slot.getStartTime().equals(updateSlotRequest.startTime())) {
            throw new ResponseStatusException(BAD_REQUEST, "La hora ingresada es igual a la actual");
        }
    }

    private void registerStudentInSlot(UUID slotId, Student student) {
        slotRepository.findById(slotId)
                .ifPresentOrElse(slot -> {
                    try {
                        addStudentToSlot(student, slot);
                        createSpecificSlotWithStudent(student, slot);
                        slotRepository.save(slot);
                    } catch (DataIntegrityViolationException e) {
                        throw new ResponseStatusException(BAD_REQUEST, "El estudiante ya se encuentra registrado en el turno solicitado");
                    } catch (Exception e) {
                        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al registrar el estudiante en el turno solicitado");
                    }
                }, () -> {
                    throw new ResponseStatusException(BAD_REQUEST, "No se encuentra registrado el turno solicitado");
                });
    }

    private static void addStudentToSlot(Student student, Slot slot) {
        slot.getStudents().add(student);
    }

    private static void createSpecificSlotWithStudent(Student student, Slot slot) {
        slot.getSpecificSlots()
                .stream()
                .filter(SlotServiceImpl::startDateIsTodayOrAfter)
                .forEach(specificSlot -> {
                    List<SpecificSlotDetail> slotDetails = specificSlot.getSpecificSlotDetails();
                    slotDetails.add(new SpecificSlotDetail(student));
                });
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
