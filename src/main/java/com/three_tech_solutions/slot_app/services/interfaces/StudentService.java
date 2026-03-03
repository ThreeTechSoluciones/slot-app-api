package com.three_tech_solutions.slot_app.services.interfaces;


import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest studentDTO);

    void deleteStudent(UUID studentId);

    StudentDetailsResponse getStudentDetails(UUID studentId);

    void activateStudent(UUID studentId);

    List<Student> getStudents();

    StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentUpdated);

    Page<StudentResponse> getStudentsByUserAndNameAndLastNameAndDni(User user, String filters, boolean filterByAbsences, StudentSituation status, Boolean isActive, Pageable pageable);

    Page<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status, Pageable pageable);

    StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId);

    void validateIfDniExists(String dni);

    Student getStudentByIdOrThrowExcepion(UUID studentId);

    void registerStudentAbsenceForSpecificSlot(UUID studentId, UUID specificSlotId);

    void recoverSlot(UUID studentId, UUID specificSlotId);

    void deleteStudentMonthlyFee(UUID studentId, UUID monthlyFeeId);
}
