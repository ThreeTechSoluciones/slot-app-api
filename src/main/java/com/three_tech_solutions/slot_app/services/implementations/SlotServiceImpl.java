package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsByDayResponse;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.mappers.SlotMapper;
import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.SlotRepository;
import com.three_tech_solutions.slot_app.services.interfaces.SlotService;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final SlotMapper slotMapper;
    private final SpecificSlotService specificSlotService;


    public SlotServiceImpl(SlotRepository slotRepository, @Lazy UserService userService, StudentService studentService, SlotMapper slotMapper, SpecificSlotService specificSlotService) {
        this.slotRepository = slotRepository;
        this.userService = userService;
        this.slotMapper = slotMapper;
        this.studentService = studentService;
        this.specificSlotService = specificSlotService;
    }

    @Override
    public void createSlot(CreateSlotRequest request) {
        User user = getUserByIdOrThrowException(request.userId());
        validateNoConflictingSlot(request.dayOfWeek(), request.startTime(),calculateEndTime(user, request.startTime()), null);
        slotRepository.save(buildSlot(request, user));
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
    public UserSlotResponse updateSlot(UUID slotId, UpdateSlotRequest updateSlotRequest) {
        Slot slot = getSlotByIdOrThrowException(slotId);
        LocalTime newStartTime = updateSlotRequest.startTime();
        LocalTime newEndTime = calculateEndTime(slot.getUser(), newStartTime);

        validateStartTimeIsDifferent(slot, newStartTime);
        validateNoConflictingSlot(slot.getDayOfWeek(), newStartTime, newEndTime, slot.getId());
        slotMapper.updateSlot(slot, updateSlotRequest);
        slot.setEndTime(newEndTime);
        updateFutureSpecificSlotsSchedule(slot, newStartTime, newEndTime);
        slotRepository.save(slot);

        return slotMapper.toSlotResponse(slot, calculateUsedCapacity(slot));
    }

    @Override
    @Transactional
    public void deleteSlot(UUID slotId) {
        Slot slot = getSlotByIdOrThrowException(slotId);
        validateSlotHasNoStudents(slot);
        deleteFutureSpecificSlotsPhysically(slot);
        logicallyDeleteSpecificSlots(slot);
        unlinkSpecificSlots(slot);
        slotRepository.delete(slot);
    }

    @Override
    public List<StudentSlotResponse> getSlotsByStudent(Student student) {
        return slotRepository.findAllByStudentOrderByDayAndTime(student)
                .stream()
                .map(slotMapper::toStudentSlotResponse)
                .toList();
    }

    @Override
    public void validateFutureSpecificSlotsCapacity(User user, byte newCapacity) {
        getFutureSpecificSlots(user).stream()
                .filter(specificSlot -> exceedsCapacity(specificSlot, newCapacity))
                .findAny()
                .ifPresent(s -> {
                    throw new ResponseStatusException(
                            BAD_REQUEST,
                            "La nueva capacidad es menor a la cantidad de alumnos inscriptos en uno o más turnos futuros"
                    );
                });
    }

    @Override
    public void updateFutureSpecificSlotsCapacity(User user, byte newCapacity) {
        getFutureSpecificSlots(user)
                .forEach(specificSlot -> specificSlot.setCapacity(newCapacity));
    }

    @Transactional
    public void removeStudentFromAllSlots(Student student) {
        List<Slot> slots = slotRepository.findAllByStudentOrderByDayAndTime(student);
        slots.forEach(slot -> slot.removeStudent(student));
        slotRepository.saveAll(slots);
    }

    @Override
    public void updateSlotsForStudent(List<UUID> slotIds, Student student) {
        List<Slot> slotsWhereToRemoveStudent = slotRepository.findSlotsWhereStudentIsRegistedAndNeedToBeRemoved(slotIds, student);
        List<Slot> slotsWhereToAddStudent = slotRepository.findAllWhereStudentIsNotRegisted(slotIds, student);

        slotsWhereToRemoveStudent.forEach(slot -> slot.removeStudent(student));
        slotsWhereToAddStudent.forEach(slot -> slot.addStudent(student));

        slotsWhereToRemoveStudent.addAll(slotsWhereToAddStudent);
        slotRepository.saveAll(slotsWhereToRemoveStudent);
    }

    private boolean exceedsCapacity(SpecificSlot specificSlot, byte newCapacity) {
        int slotUsedCapacity = calculateUsedCapacity(specificSlot.getSlot());
        int specificSlotUsedCapacity = specificSlot.getSpecificSlotUsedCapacity();

        return newCapacity < slotUsedCapacity || newCapacity < specificSlotUsedCapacity;
    }

    private List<SpecificSlot> getFutureSpecificSlots(User user) {
        LocalDate today = LocalDate.now();
        return user.getSlots().stream()
                .flatMap(slot -> slot.getSpecificSlots().stream())
                .filter(specificSlot ->
                        !specificSlot.getSlotDate().isBefore(today)
                )
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

    private Slot getSlotByIdOrThrowException(UUID slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "No se encontró el turno"));
    }

    private Slot buildSlot(CreateSlotRequest request, User user) {
        return new Slot(
                request.dayOfWeek(),
                request.startTime(),
                calculateEndTime(user, request.startTime()),
                user.getUserPreferences().getSlotCapacity(),
                user,
                createSpecificSlots(
                        request,
                        user.getUserPreferences().getSlotDurationMinutes(),
                        user.getUserPreferences().getSlotCapacity(),
                        user
                )
        );
    }

    private List<SpecificSlot> createSpecificSlots(
            CreateSlotRequest request,
            long slotDurationMinutes,
            byte slotCapacity,
            User user
    ) {
        List<SpecificSlot> specificSlots = new ArrayList<>();
        LocalDate date = getNextOrSameDateOfDayOfWeek(request);
        LocalDate endDate = date.plusMonths(2);

        while (dateIsWithinSlotCreationPeriod(date, endDate)) {
            /*
             * This is to evict create slots with a start time
             * before now when the day of week is the same as today
             */
            if (slotTimeIsBeforeNow(request, date)) {
                date = date.plusWeeks(1);
                continue;
            }

            specificSlots.add(buildSpecificSlot(request, date, slotCapacity, slotDurationMinutes, user));
            date = date.plusWeeks(1);
        }

        return specificSlots;
    }

    private static boolean dateIsWithinSlotCreationPeriod(LocalDate date, LocalDate endDate) {
        return date.isBefore(endDate) || date.isEqual(endDate);
    }

    private static boolean slotTimeIsBeforeNow(CreateSlotRequest request, LocalDate date) {
        return date.isEqual(LocalDate.now()) && request.startTime().isBefore(LocalTime.now());
    }

    private SpecificSlot buildSpecificSlot(
            CreateSlotRequest request,
            LocalDate startDate,
            byte slotCapacity,
            long slotDurationMinutes,
            User user
    ) {
        return new SpecificSlot(
                startDate,
                slotCapacity,
                request.startTime(),
                request.startTime().plusMinutes(slotDurationMinutes),
                user,
                SpecificSlotStatus.CREATED
        );
    }

    private static LocalDate getNextOrSameDateOfDayOfWeek(CreateSlotRequest request) {
        return LocalDate.now().with(TemporalAdjusters.nextOrSame(request.dayOfWeek()));
    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }

    private void validateNoConflictingSlot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, UUID excludedSlotId) {
        if (slotRepository.existsWithinRange(dayOfWeek, startTime, endTime, excludedSlotId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un turno que coincide con el día y el rango de horario ingresado");
        }
    }

    private LocalTime calculateEndTime(User user, LocalTime startTime) {
        return startTime.plusMinutes(user.getUserPreferences().getSlotDurationMinutes());
    }

    private void validateStartTimeIsDifferent(Slot slot, LocalTime newStartTime) {
        if (slot.getStartTime().equals(newStartTime)) {
            throw new ResponseStatusException(BAD_REQUEST, "La hora ingresada es igual a la actual");
        }
    }

    private void registerStudentInSlot(UUID slotId, Student student) {
        slotRepository.findById(slotId)
                .ifPresentOrElse(slot -> {
                    try {
                        validateSlotCapacity(slot);
                        slot.addStudent(student);
                        slotRepository.save(slot);
                    } catch (DataIntegrityViolationException e) {
                        throw new ResponseStatusException(BAD_REQUEST, "El estudiante ya se encuentra registrado en el turno solicitado");
                    } catch (ResponseStatusException e) {
                        throw e;
                    } catch (Exception e) {
                        log.error("Error al registrar el estudiante {} en el turno {}: {}", student.getId(), slotId, e.getMessage());
                        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al registrar el estudiante en el turno solicitado");
                    }
                }, () -> {
                    throw new ResponseStatusException(BAD_REQUEST, "No se encuentra registrado el turno solicitado");
                });
    }

    private static void validateSlotCapacity(Slot slot) {
        if (slot.isAtFullCapacity()) {
            throw new ResponseStatusException(BAD_REQUEST, "El turno ya se encuentra completo");
        }

        if (slot.someSpecificSlotIsAtFullCapacity()) {
            throw new ResponseStatusException(BAD_REQUEST, "Una o más fechas están completas debido a recuperaciones de clase.");
        }
    }

    private void validateSlotHasNoStudents(Slot slot) {
        if (slot.hasAtLeastOneStudentRegisted()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "No se puede eliminar el turno porque tiene alumnos asignados o recuperando"
            );
        }
    }

    private void deleteFutureSpecificSlotsPhysically(Slot slot) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<SpecificSlot> toDelete = slot.getSpecificSlots().stream()
                .filter(specificSlot ->
                        specificSlot.getSlotDate().isAfter(today) ||
                                (specificSlot.getSlotDate().isEqual(today)
                                        && specificSlot.getStartTime().isAfter(now))
                )
                .toList();
        specificSlotService.deleteSpecificSlots(toDelete);
        slot.getSpecificSlots().removeAll(toDelete);
    }

    private void logicallyDeleteSpecificSlots(Slot slot) {
        slot.getSpecificSlots()
                .forEach(specificSlot -> specificSlot.setStatus(SpecificSlotStatus.DELETED));
    }

    private void unlinkSpecificSlots(Slot slot) {
        slot.getSpecificSlots()
                .forEach(specificSlot ->
                        specificSlot.setSlot(null));
    }

    private void updateFutureSpecificSlotsSchedule(Slot slot, LocalTime newStartTime, LocalTime newEndTime) {
        slot.getFutureSpecificSlots()
                .forEach(specificSlot -> {
                    specificSlot.setStartTime(newStartTime);
                    specificSlot.setEndTime(newEndTime);
                });
    }
}
