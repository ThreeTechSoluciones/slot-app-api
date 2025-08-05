package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                studentDTO.getAdmissionDate(),
                true,
                plan,
                user
        );
    }


    public StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getName(),
                student.getLastname(),
                student.isEnabled(),
                student.getId()
        );
    }

    public List<StudentResponse> toResponseList(List<Student> students) {
        return students.stream()
                .map(this::toStudentResponse)
                .toList();
    }

    public StudentDetailsResponse toStudentDetailsResponse(Student student) {

        return new StudentDetailsResponse(
                student.getId(),
                student.getName(),
                student.getLastname(),
                student.getDni(),
                student.getPhoneNumber(),
                student.getBirthday(),
                student.getPathologies(),
                student.getAdmissionDate(),
                student.getPlan().getPlanType().getName(),
                student.getPlan().getClassesPerWeek(),
                student.getPlan().getPaymentDay(),
                student.getPayments().stream().map(this::toPaymentDetailsResponse).collect(Collectors.toList())
        );
    }

    private PaymentDetailsResponse toPaymentDetailsResponse(Payment payment) {
        return new PaymentDetailsResponse(
                payment.getId(),
                payment.getNumber(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getExpirationDate()
        );
    }
}
