package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.PaymentPlan;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentMapper {


    public Student toStudent(CreateStudentRequest studentDTO, Plan plan, User user) {
        return new Student(
                studentDTO.getName(),
                studentDTO.getLastName(),
                studentDTO.getDni(),
                studentDTO.getCellphoneNumber(),
                studentDTO.getBirthday(),
                studentDTO.getPathologies(),
                user,
                new PaymentPlan(
                        studentDTO.getPaymentDay(),
                        studentDTO.getPaymentPlanName(),
                        plan
                )
        );
    }


    public StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getName(),
                student.getLastname(),
                student.getDni(),
                student.isEnabled(),
                student.getId()
        );
    }

    public List<StudentResponse> toResponseList(List<Student> students) {
        return students.stream()
                .map(this::toStudentResponse)
                .toList();
    }

    public StudentDetailsResponse toStudentDetailsResponse(Student student, Integer age) {
        return new StudentDetailsResponse(
                student.getId(),
                student.getName(),
                student.getLastname(),
                student.getDni(),
                student.getPhoneNumber(),
                student.getBirthday(),
                age,
                student.getPathologies(),
                student.getAdmissionDate(),
                student.getPaymentPlan().getPaymentPlanName().getName(),
                student.getPaymentPlan().getPlan().getName(),
                student.getPaymentPlan().getPlan().getNumberOfDays(),
                student.getPaymentPlan().getPaymentDay(),
                student.isEnabled(),
                student.getStudentSituation(),
                student.getPaymentPlan().getPlan().getId()
        );
    }



    public void updateStudent(Student student, UpdateStudentRequest request, Plan plan) {
        student.setName(request.getName());
        student.setLastname(request.getLastName());
        student.setDni(request.getDni());
        student.setPhoneNumber(request.getCellphoneNumber());
        student.getPaymentPlan().setPaymentPlanName(request.getPaymentPlanName());
        student.getPaymentPlan().setPaymentDay(request.getPaymentDay());
        student.getPaymentPlan().setPlan(plan);
        student.setBirthday(request.getBirthday());
        student.setPathologies(request.getPathologies());
    }
}
