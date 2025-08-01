package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.UpdateStudentRequest;

import java.util.List;

public class UpdateStudentMapper {

        public static void updateStudentFromRequest(Student student, UpdateStudentRequest request) {
            student.setName(request.getName());
            student.setLastname(request.getLastName());
            student.setDni(request.getDni());
            student.setPhoneNumber(request.getCellphoneNumber());
            student.getPlan().setPlanType(request.getPlanType());
            student.getPlan().setClassesPerWeek(request.getClassesPerWeek());
            student.getPlan().setPaymentDay(request.getPaymentDay());
            student.setBirthday(request.getBirthday());
            student.setAdmissionDate(request.getAdmissionDate());
            student.setPathologies(request.getPathologies());

        }

        
}
