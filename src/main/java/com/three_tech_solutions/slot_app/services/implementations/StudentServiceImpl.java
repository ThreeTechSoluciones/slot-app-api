package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.controllers.requests.ActivateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.data.enums.*;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.*;
import com.three_tech_solutions.slot_app.data.repositories.StudentRepository;
import com.three_tech_solutions.slot_app.exceptions.custom_exceptions.StudentAlreadyRegisteredException;
import com.three_tech_solutions.slot_app.services.interfaces.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.three_tech_solutions.slot_app.utils.PaymentPlanUtils.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserService userService;
    private final MonthlyFeeService monthlyFeeService;
    private final SlotService slotService;
    private final PlanService planService;
    private final SpecificSlotDetailService specificSlotDetailService;
    private final SpecificSlotService specificSlotService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudentMapper studentMapper,
            @Lazy UserService userService,
            @Lazy MonthlyFeeService monthlyFeeService,
            @Lazy PlanService planService,
            @Lazy SlotService slotService,
            SpecificSlotDetailService specificSlotDetailService,
            @Lazy SpecificSlotService specificSlotService
    ) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userService = userService;
        this.monthlyFeeService = monthlyFeeService;
        this.planService = planService;
        this.slotService = slotService;
        this.specificSlotDetailService = specificSlotDetailService;
        this.specificSlotService = specificSlotService;
    }

    @Transactional
    @Override
    public StudentResponse createStudent(CreateStudentRequest studentDTO) {
        validatePlanAndPaymentDay(studentDTO.getPaymentPlanName(), studentDTO.getPaymentDay(), studentDTO.getExtraClasses(), studentDTO.getClassPrice());

        try{
            Student student = studentMapper.toStudent(
                    studentDTO,
                    buildStudentPaymentPlan(studentDTO.getPaymentPlanName(), studentDTO.getPaymentDay(), studentDTO.getPlanId()),
                    getUserByIdOrThrowException(studentDTO.getUserId())
            );

            studentRepository.save(student);
            createInitialMonthlyFee(student, getInitialPaymentContext(studentDTO.getClassPrice(), studentDTO.getExtraClasses()));
            addStudentToSlots(studentDTO.getSlotIds(), student);
            return studentMapper.toStudentResponse(student);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(BAD_REQUEST, "El DNI ya existe");
        } catch (ResponseStatusException exception) {
            log.error("Error al crear el estudiante: {}", exception.getReason());
            throw exception;
        } catch (Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,"Ocurrió un error al registrar el estudiante. Intente nuevamente");
        }
    }

    @Override
    public StudentDetailsResponse getStudentDetails(UUID studentId) {
        Student student = getStudentByIdOrThrowExcepion(studentId);
        List<StudentSlotResponse> slots = getStudentSlots(student);
        return studentMapper.toStudentDetailsResponse(student, slots , calculateStudentAge(student.getBirthday()));
    }

    @Override
    @Transactional
    public StudentResponse activateStudent(UUID studentId, ActivateStudentRequest activateStudentRequest) {
        validatePlanAndPaymentDay(
                activateStudentRequest.paymentPlanName(),
                activateStudentRequest.paymentDay(),
                activateStudentRequest.extraClasses(),
                activateStudentRequest.classPrice()
        );

        return studentMapper.toStudentResponse(
                studentRepository.save(
                        getStudentAndSetNewPaymentInformationAndSlots(studentId, activateStudentRequest)
                )
        );
    }

    private Student getStudentAndSetNewPaymentInformationAndSlots(UUID studentId, ActivateStudentRequest activateStudentRequest) {
        Student student = getStudentAndValidateIfIsNotAlreadyActivated(studentId);
        setNewPaymentInfoAndSlotsToStudent(activateStudentRequest, student);
        return student;
    }

    private Student getStudentAndValidateIfIsNotAlreadyActivated(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
        validateStudentIsNotAlreadyActivated(student);
        return student;
    }

    private void setNewPaymentInfoAndSlotsToStudent(ActivateStudentRequest activateStudentRequest, Student student) {
        student.setPaymentPlan(buildStudentPaymentPlan(activateStudentRequest.paymentPlanName(), activateStudentRequest.paymentDay(), activateStudentRequest.planId()));
        addStudentToSlots(activateStudentRequest.slotIds(), student);
        createInitialMonthlyFee(student, getInitialPaymentContext(activateStudentRequest.classPrice(), activateStudentRequest.extraClasses()));
        student.setEnabled(true);
    }

    private static InitialPaymentContext getInitialPaymentContext(Double classPrice, Byte extraClasses) {
        return new InitialPaymentContext(classPrice, extraClasses);
    }

    private static void validateStudentIsNotAlreadyActivated(Student student) {
        if(student.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estudiante ya se encuentra activo.");
        }
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteStudent(UUID studentId){
        Student student = getStudentByIdOrThrowExcepion(studentId);
        validateStudentIsEnabled(student);
        removeStudentFromAllSlots(student);
        deleteFutureNonRecurrentSpecificSlotDetails(studentId);
        student.setPaymentPlan(null);
        student.setEnabled(false);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(UUID studentId, UpdateStudentRequest request) {
        validatePaymentDay(request.getPaymentPlanName(), request.getPaymentDay());
        return studentRepository.findById(studentId)
                .map(student -> {
                    removePaymentDayIfNewPlanIsBeginningOfMonth(request);
                    studentMapper.updateStudent(student,request, getPlanByIdOrThrowException(request.getPlanId()));
                    slotService.updateSlotsForStudent(request.getSlotIds(), student);

                    return studentMapper.toStudentResponse(studentRepository.save(student));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe."));
    }

    private void removePaymentDayIfNewPlanIsBeginningOfMonth(UpdateStudentRequest studentUpdated) {
        if (planTypeIsBeginningOfMonth(studentUpdated.getPaymentPlanName())) {
            studentUpdated.setPaymentDay(null);
        }
    }

    @Override
    public Page<StudentResponse> getStudentsByUserAndNameAndLastNameAndDni(User user, String filters, boolean filterByAbsences, StudentSituation status, Boolean isActive, Pageable pageable) {
        Page<StudentResponse> studentsPage = studentRepository
                .getStudentsByUserAndFilters(user, filters, filterByAbsences, isActive, pageable)
                .map(studentMapper::toStudentResponse);
        List<StudentResponse> filteredContent = getContentByStudentSituationFilter(status, studentsPage);

        return new PageImpl<>(
                filteredContent,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                getContentSize(status, studentsPage, filteredContent)
        );
    }

    private static long getContentSize(StudentSituation status, Page<StudentResponse> studentsPage, List<StudentResponse> filteredContent) {
        // This is because when we apply the student situation filter,
        // we are filtering the content of the page, but the total elements of the page
        // still corresponds to the total elements without applying the student situation filter.
        // So we need to adjust the total elements of the page to correspond to the filtered content size.
        return status == null ? studentsPage.getTotalElements() : filteredContent.size();
    }

    private static List<StudentResponse> getContentByStudentSituationFilter(StudentSituation status, Page<StudentResponse> studentsPage) {
        return status != null ? filterStudentPageBySituationFilter(status, studentsPage) : studentsPage.getContent();
    }

    private static List<StudentResponse> filterStudentPageBySituationFilter(StudentSituation status, Page<StudentResponse> studentsPage) {
        return studentsPage.getContent().stream().filter(studentResponse -> studentResponse.status().equals(status)).toList();
    }

    @Override
    public Page<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status, Pageable pageable) {
        return studentRepository
                .findById(studentId)
                .map(student -> monthlyFeeService.getMonthlyFeesByStudent(student, month, expirationDate, status, pageable))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
    }

    @Override
    public StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId) {
        return studentRepository
                .findById(studentId)
                .map(monthlyFeeService::createMonthlyFeeForStudent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
    }

    @Override
    public void validateIfDniExists(String dni) {
        if (studentRepository.existsByDni(dni)) {
            throw new ResponseStatusException(BAD_REQUEST, "El DNI ya está registrado");
        }
    }

    @Override
    public Student getStudentByIdOrThrowExcepion(UUID studentId) {
        return this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
    }

    private Integer calculateStudentAge(LocalDate birthday) {
        return Optional.ofNullable(birthday)
                .map(date -> Period.between(date, LocalDate.now()).getYears())
                .orElse(null);
    }

    private void createInitialMonthlyFee(Student student, InitialPaymentContext initialPaymentContext) {
        monthlyFeeService.createInitialMonthlyFee(student, initialPaymentContext);
    }

    private void addStudentToSlots(List<UUID> slotIds, Student student) {
        slotIds.forEach(slotId -> slotService.addStudentToSlot(slotId, student));
    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }

    private Plan getPlanByIdOrThrowException(UUID planId) {
        return planService.getPlanByIdOrThrowException(planId);
    }

    @Override
    @Transactional
    public void registerStudentAbsenceForSpecificSlot(UUID studentId, UUID specificSlotId) {
        this.specificSlotDetailService.getSpecificSlotDetailBySpecificSlotIdAndStudentId(specificSlotId, studentId)
                .ifPresentOrElse(
                        specificSlotDetail -> {
                            validateIfStudentIsNotAlreadyRegisteredAsAbsent(specificSlotDetail);
                            changeStatusToAbsence(specificSlotDetail);
                            registerNewStudentAbsence(studentId, specificSlotDetail);
                        },
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no está registrado en el turno especificado.");
                        }
                );

    }

    @Override
    @Transactional
    public void recoverSlot(UUID studentId, UUID specificSlotId) {
        SpecificSlot specificSlot = specificSlotService.getSpecificSlotByIdOrThrowException(specificSlotId);
        Student student = getStudentByIdOrThrowExcepion(studentId);
        validateRecoverySlotIsNotRegularSlot(student, specificSlot);
        student.registerAbsenceAsRecovered();
        specificSlot.addStudent(student);
        specificSlotService.saveSpecificSlot(specificSlot);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudentMonthlyFee(UUID studentId, UUID monthlyFeeId) {
        getStudentByIdOrThrowExcepion(studentId);
        MonthlyFee monthlyFee = monthlyFeeService.getMonthlyFeeById(monthlyFeeId);
        validateIfMonthlyFeeCanBeDeleted(monthlyFee);
        monthlyFeeService.deleteMonthlyFee(monthlyFee);
    }

    private void registerNewStudentAbsence(UUID studentId, SpecificSlotDetail specificSlotDetail) {
        Student student = getStudentByIdOrThrowExcepion(studentId);
        buildStudentAbsence(specificSlotDetail, student);
        studentRepository.save(student);
    }

    private static void buildStudentAbsence(SpecificSlotDetail specificSlotDetail, Student student) {
        student.getAbsences().add(new Absence(
                specificSlotDetail.getSpecificSlot().getSlotDate(),
                AbsenceStatus.PENDING,
                specificSlotDetail.getSpecificSlot().getStartTime(),
                specificSlotDetail.getSpecificSlot().getEndTime()
        ));
    }

    private void changeStatusToAbsence(SpecificSlotDetail specificSlotDetail) {
        specificSlotDetailService.registerAbsence(specificSlotDetail);
    }

    private static void validateIfStudentIsNotAlreadyRegisteredAsAbsent(SpecificSlotDetail specificSlotDetail) {
        if(specificSlotDetailStatusIsAbsence(specificSlotDetail)) {
            throw new StudentAlreadyRegisteredException();
        }
    }

    private static boolean specificSlotDetailStatusIsAbsence(SpecificSlotDetail specificSlotDetail) {
        return specificSlotDetail.getStatus().equals(SpecificSlotDetailStatus.ABSENCE);
    }

    private void validatePlanDetail(PaymentPlanName paymentPlanName, Byte paymentDay, Byte extraClasses, Double classPrice) {

        if (planTypeIsBeginningOfMonth(paymentPlanName) & paymentDay!= null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se debe especificar día de pago para el plan 'Principio de mes'.");
        }

        if (planTypeIsBeginningOfMonth(paymentPlanName) &&
                LocalDate.now().getDayOfMonth() > BEGINNING_OF_MONTH_EXPIRATION_DATE &&
                extraClasses == null &&
                classPrice == null
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar las clases extras y el precio por clase.");
        }
    }

    private void validatePaymentDay(PaymentPlanName paymentPlanName, Byte paymentDay){
        if (planTypeIsSpecificDay(paymentPlanName) && paymentDayIsInvalid(paymentDay)) {
            throw new ResponseStatusException(BAD_REQUEST, "El día de pago debe ser entre 11 y 28.");
        }
    }

    private boolean paymentDayIsInvalid(Byte paymentDay) {
        return paymentDay == null || paymentDay <= SPECIFIC_DAY_MINIMUM_DAY || paymentDay > SPECIFIC_DAY_MAXIMUM_DAY;
    }

    private boolean planTypeIsSpecificDay(PaymentPlanName paymentPlanName) {
        return PaymentPlanName.SPECIFIC_DAY.equals(paymentPlanName);
    }

    private boolean planTypeIsBeginningOfMonth(PaymentPlanName paymentPlanName){
        return PaymentPlanName.BEGINNING_OF_MONTH.equals(paymentPlanName);
    }

    private List<StudentSlotResponse> getStudentSlots(Student student) {
        return slotService.getSlotsByStudent(student);
    }

    private void validateStudentIsEnabled(Student student) {
        if (!student.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estudiante ya se encuentra eliminado.");
        }
    }

    private void removeStudentFromAllSlots(Student student) {
        slotService.removeStudentFromAllSlots(student);
    }

    private void deleteFutureNonRecurrentSpecificSlotDetails(UUID studentId) {
        specificSlotDetailService.deleteFutureNonRecurrentSpecificSlotDetails(studentId);
    }


    private void validatePlanAndPaymentDay(
            PaymentPlanName paymentPlanName,
            Byte paymentDay,
            Byte extraClasses,
            Double classPrice
    ) {
        validatePlanDetail(paymentPlanName, paymentDay, extraClasses, classPrice);
        validatePaymentDay(paymentPlanName, paymentDay);
    }

    private PaymentPlan buildStudentPaymentPlan(
            PaymentPlanName paymentPlanName,
            Byte paymentDay,
            UUID planId
    ) {
        return new PaymentPlan(
                getPaymentDay(paymentPlanName, paymentDay),
                paymentPlanName,
                getPlanByIdOrThrowException(planId)
        );
    }

    private static Byte getPaymentDay(
            PaymentPlanName paymentPlanName,
            Byte paymentDay
    ) {
        return paymentPlanName == PaymentPlanName.SPECIFIC_DAY ? paymentDay : BEGINNING_OF_MONTH_EXPIRATION_DATE;
    }

    private void validateRecoverySlotIsNotRegularSlot(Student student, SpecificSlot specificSlot) {

        boolean studentAlreadyRegisteredInSlot =
                specificSlot.getSlot()
                        .getStudents()
                        .contains(student);

        if (studentAlreadyRegisteredInSlot) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede recuperar en un turno en el que el estudiante ya está inscripto."
            );
        }
    }

    private void validateIfMonthlyFeeCanBeDeleted(MonthlyFee monthlyFee) {
        if (monthlyFee.getCurrentStatus() == MonthlyFeeStatus.PAYED ||
                monthlyFee.getCurrentStatus() == MonthlyFeeStatus.PAYED_OUT_OF_TIME) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar una cuota que ya fue pagada."
            );
        }
    }

}