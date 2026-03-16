package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.models.*;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.three_tech_solutions.slot_app.utils.NullSafeUtils.getValueOrNull;

@Service
public class StudentMapper {


    public Student toStudent(CreateStudentRequest studentDTO, PaymentPlan paymentPlan, User user) {
        return new Student(
                studentDTO.getName(),
                studentDTO.getLastName(),
                studentDTO.getDni(),
                studentDTO.getCellphoneNumber(),
                studentDTO.getBirthday(),
                studentDTO.getPathologies(),
                user,
                paymentPlan
        );
    }


    public StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getName(),
                student.getLastname(),
                student.getDni(),
                student.getStudentSituation(),
                student.isEnabled(),
                getNumberOfSlotsToRecover(student),
                student.getId()
        );
    }

    public static SpecificSlotResponse.Student buildStudentResponse(SpecificSlotDetail specificSlotDetail) {
        return new SpecificSlotResponse.Student(
                specificSlotDetail.getStudent().getId(),
                specificSlotDetail.getStudent().getName() + " " + specificSlotDetail.getStudent().getLastname(),
                specificSlotDetail.getStatus()
        );
    }

    public StudentDetailsResponse toStudentDetailsResponse(
            Student student,
            List<StudentSlotResponse> slots,
            Integer age
    ) {

        PaymentPlan paymentPlan = student.getPaymentPlan();

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
                getValueOrNull(paymentPlan, p -> p.getPaymentPlanName().getName()),
                getValueOrNull(paymentPlan, p -> p.getPlan().getName()),
                getValueOrNull(paymentPlan, p -> p.getPlan().getNumberOfDays()),
                getValueOrNull(paymentPlan, PaymentPlan::getPaymentDay),
                student.isEnabled(),
                student.getStudentSituation(),
                getValueOrNull(paymentPlan, p -> p.getPlan().getId()),
                slots
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


    private int getNumberOfSlotsToRecover(Student student) {
        return student.getAbsences().stream().filter(absence -> absence.getStatus() == AbsenceStatus.PENDING).toList().size();
    }

}
