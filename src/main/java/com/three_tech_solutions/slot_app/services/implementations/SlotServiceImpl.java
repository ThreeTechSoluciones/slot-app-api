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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        validateNoConflictingSlot(request.dayOfWeek(), request.startTime(), null);
        slotRepository.save(buildSlot(request));
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
        validateStartTimeIsDifferent(slot, updateSlotRequest);
        validateNoConflictingSlot(slot.getDayOfWeek(), updateSlotRequest.startTime(), slot.getId());
        slotMapper.updateSlot(slot, updateSlotRequest);
        slot.setEndTime(calculateEndTime(slot.getUser(), slot.getStartTime()));
        slotRepository.save(slot);
        return slotMapper.toSlotResponse(slot, calculateUsedCapacity(slot));
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

    @Override
    public void validateFutureSpecificSlotsCapacity(User user, byte newCapacity) {
        getFutureSpecificSlots(user).stream()
                .filter(specificSlot ->
                        specificSlot.getSpecificSlotDetails().size() > newCapacity
                )
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

    private static boolean startDateIsTodayOrAfter(SpecificSlot specificSlot) {
        return specificSlot.getSlotDate().isEqual(LocalDate.now()) || specificSlot.getSlotDate().isAfter(LocalDate.now());
    }

    private Slot getSlotByIdOrThrowException(UUID slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "No se encontró el turno"));
    }

    private Slot buildSlot(CreateSlotRequest request) {
        User user = getUserByIdOrThrowException(request.userId());
        return new Slot(
                request.dayOfWeek(),
                request.startTime(),
                calculateEndTime(user, request.startTime()),
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

    private void validateNoConflictingSlot(DayOfWeek dayOfWeek, LocalTime startTime, UUID excludedSlotId) {
        if (slotRepository.existsWithinRange(startTime, dayOfWeek, excludedSlotId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Ya existe un turno que coincide con el día y horario ingresado");
        }
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
