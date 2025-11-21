package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.StudentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.PlanService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.three_tech_solutions.slot_app.components.monthly_fee_processors.implementations.BeginningOfMonthMonthlyFeeProcessor.BEGINNING_OF_MONTH_EXPIRATION_DATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserService userService;
    private final MonthlyFeeService monthlyFeeService;
    private final PlanService planService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            StudentMapper studentMapper,
            @Lazy UserService userService,
            @Lazy MonthlyFeeService monthlyFeeService,
            @Lazy PlanService planService
    ) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userService = userService;
        this.monthlyFeeService = monthlyFeeService;
        this.planService = planService;
    }


    @Transactional
    @Override
    public StudentResponse createStudent(CreateStudentRequest studentDTO) {
        validatePlanDetail(studentDTO.getPaymentPlanName(), studentDTO.getPaymentDay(), studentDTO.getExtraClasses(), studentDTO.getClassPrice());

        Plan plan = getPlanByIdOrThrowException(studentDTO.getPlanId());
        User user = getUserByIdOrThrowException(studentDTO.getUserId());

        Student student = studentMapper.toStudent(studentDTO, plan, user);

        try{
            studentRepository.save(student);
            monthlyFeeService.createInitialMonthlyFee(student, studentDTO);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(BAD_REQUEST, "El DNI ya existe");
        } catch (Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,"Ocurrió un error al registrar el estudiante. Intente nuevamente");
        }
        return studentMapper.toStudentResponse(student);

    }

    private User getUserByIdOrThrowException(UUID userId) {
        return userService.getUserByIdOrThrowException(userId);
    }

    private Plan getPlanByIdOrThrowException(UUID planId) {
        return planService.getPlanByIdOrThrowException(planId);
    }

    @Override
    public StudentDetailsResponse getStudentById(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));

        return studentMapper.toStudentDetailsResponse(student);
    }

    @Override
    public void activateStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));


        student.setEnabled(true);
        studentRepository.save(student);
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudent(UUID studentId){
        studentRepository.findById(studentId)
                .map(student -> {
                    if (!student.isEnabled()) {
                        throw new ResponseStatusException(BAD_REQUEST, "El estudiante ya está eliminado.");
                    }
                    student.setEnabled(false);
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe."));
    }

    @Override
    public StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentUpdated) {

        return studentRepository.findById(studentId)
                .map(student -> {
                    if (planTypeIsBeginningOfMonth(studentUpdated.getPaymentPlanName())) {
                        studentUpdated.setPaymentDay(null);
                    }
                    validatePlanDetail(studentUpdated.getPaymentPlanName(), studentUpdated.getPaymentDay(), studentUpdated.getExtraClasses(), studentUpdated.getClassPrice());
                    studentMapper.updateStudent(
                            student,
                            studentUpdated,
                            getPlanByIdOrThrowException(studentUpdated.getPlanId())
                    );
                    studentRepository.save(student);
                    return studentMapper.toStudentResponse(student);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe."));
    }

    @Override
    public List<Student> getStudentsByUserAndNameAndLastNameAndDni(User user, String filters) {
        return studentRepository.getStudentsByUserAndNameAndLastnameAndDni(user, filters);
    }

    public List<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status) {
        return studentRepository
                .findById(studentId)
                .map(student -> monthlyFeeService.getMonthlyFeesByStudent(student, month, expirationDate, status))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
    }

    @Override
    public StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId) {
        return studentRepository
                .findById(studentId)
                .map(student -> monthlyFeeService.createMonthlyFeeForStudent(student))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe"));
    }

    @Override
    public void validateIfDniExists(String dni) {
        if (!studentRepository.existsByDni(dni)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El DNI no está registrado");
        }
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

        if (planTypeIsSpecificDay(paymentPlanName) && paymentDayIsInvalid(paymentDay)) {
            throw new ResponseStatusException(BAD_REQUEST, "El día de pago debe ser entre 11 y 28.");
        }
    }

    private boolean paymentDayIsInvalid(Byte paymentDay) {
        return paymentDay == null || paymentDay <= 10 || paymentDay > 28;
    }

    private boolean planTypeIsSpecificDay(PaymentPlanName paymentPlanName) {
        return PaymentPlanName.SPECIFIC_DAY.equals(paymentPlanName);
    }

    private boolean planTypeIsBeginningOfMonth(PaymentPlanName paymentPlanName){
        return PaymentPlanName.BEGINNING_OF_MONTH.equals(paymentPlanName);
    }
}