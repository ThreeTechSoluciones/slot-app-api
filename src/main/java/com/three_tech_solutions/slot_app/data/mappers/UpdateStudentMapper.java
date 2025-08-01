package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;

public class UpdateStudentMapper {
    public static void updateStudentFromRequest(Student student, UpdateStudentRequest request) {
        student.setName(request.getName());
        student.setLastname(request.getLastName());
        student.setDni(request.getDni());
        student.setPhoneNumber(request.getCellphoneNumber());
        student.getPlan().setPlanType(request.getPlanType());
        student.getPlan().setPaymentDay(request.getPaymentDay());
        student.setBirthday(request.getBirthday());
        student.setPathologies(request.getPathologies());
    }
}
