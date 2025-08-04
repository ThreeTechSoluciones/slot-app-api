package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        validatePlanDetail(studentDTO);

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

    private void validatePlanDetail(CreateStudentRequest studentDTO) {
        //El día de pago debe ser un dato opcional (obligatorio en caso de que el tipo de pago sea dia específico) y debe ser mayor o igual a 1 y menor o igual a 31
        if (planTypeIsBeginningOfMonth(studentDTO) & studentDTO.getPaymentDay() != null) {
            throw new ResponseStatusException(BAD_REQUEST, "No debe especificar día de pago para el plan 'Principio de mes'.");
        }

        if (planTypeIsSpecificDay(studentDTO) && paymentDayIsInvalid(studentDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El día de pago debe ser entre 11 y 28.");
        }
    }

    private boolean paymentDayIsInvalid(CreateStudentRequest studentDTO) {
        return studentDTO.getPaymentDay() <= 10 || studentDTO.getPaymentDay() > 28;
    }

    private boolean planTypeIsSpecificDay(CreateStudentRequest studentDTO) {
        return studentDTO.getPlanType().equals(PlanType.DIA_ESPECIFICO);
    }

    private boolean planTypeIsBeginningOfMonth(CreateStudentRequest studentDTO) {
        return studentDTO.getPlanType().equals(PlanType.PRINCIPIO_DE_MES);
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
}
