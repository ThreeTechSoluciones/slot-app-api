package com.three_tech_solutions.slot_app.mappers;

import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.dto.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.dto.StudentResponse;
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
        StudentResponse studentDTO = new StudentResponse();
        studentDTO.setName(student.getName());
        studentDTO.setLastName(student.getLastname());

        return studentDTO;
    }

    public StudentDetailsResponse toStudentDetailsResponse(Student student) {

        return new StudentDetailsResponse(
                student.getId(),
                student.getName(),
                student.getLastname(),
                student.getPhoneNumber(),
                student.getBirthday(),
                student.getPathologies(),
                student.getAdmissionDate(),
                student.getPlan().getPlanType().name(),
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
