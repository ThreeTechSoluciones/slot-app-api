package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.data.repositories.StudentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserService userService;

    @Override
    public StudentResponse createStudent(CreateStudentRequest studentDTO) {
        validatePlanDetail(studentDTO.getPlanType(), studentDTO.getPaymentDay());

        Plan plan = new Plan();
        plan.setId(UUID.randomUUID());
        plan.setClassesPerWeek(studentDTO.getClassesPerWeek());
        plan.setPaymentDay(studentDTO.getPaymentDay());
        plan.setPlanType(studentDTO.getPlanType());

        //Al registrar el alumno, debe vincularse con el usuario (profesional logueado)
        User user = userService.getUserByIdOrThrowException(studentDTO.getUserId());

        Student student = studentMapper.toStudent(studentDTO, plan, user);

        try{
            studentRepository.save(student);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(BAD_REQUEST, "El DNI ya existe");
        } catch (Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,"Ocurrió un error al registrar el estudiante. Intente nuevamente");
        }
        return studentMapper.toStudentResponse(student);

    }

    @Override
    public StudentDetailsResponse getStudentById(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado"));

        return studentMapper.toStudentDetailsResponse(student);
    }

    @Override
    public void activateStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));

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
        validatePlanDetail(studentUpdated.planType(), studentUpdated.paymentDay());
        return studentRepository.findById(studentId)
                .map(student -> {
                    studentMapper.updateStudent(student, studentUpdated);
                    studentRepository.save(student);
                    return studentMapper.toStudentResponse(student);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe."));
    }

    private void validatePlanDetail(PlanType planType, Byte paymentDay) {

        if (planTypeIsBeginningOfMonth(planType) & paymentDay!= null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No debe especificar día de pago para el plan 'Principio de mes'.");
        }

        if (planTypeIsSpecificDay(planType) && paymentDayIsInvalid(paymentDay)) {
            throw new ResponseStatusException(BAD_REQUEST, "El día de pago debe ser entre 11 y 28.");
        }
    }

    private boolean paymentDayIsInvalid(Byte paymentDay) {
        return paymentDay <= 10 || paymentDay > 28;
    }

    private boolean planTypeIsSpecificDay(PlanType planType) {
        return PlanType.DIA_ESPECIFICO.equals(planType);
    }

    private boolean planTypeIsBeginningOfMonth(PlanType planType){
        return PlanType.PRINCIPIO_DE_MES.equals(planType);
    }

}