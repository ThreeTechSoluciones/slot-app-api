package com.three_tech_solutions.slot_app.services.interfaces;


import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.requests.UpdateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(CreateStudentRequest studentDTO);

    void deleteStudent(UUID studentId);

    StudentDetailsResponse getStudentById(UUID studentId);

    void activateStudent(UUID studentId);

    List<Student> getStudents();

    StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentUpdated);

    List<Student> getStudentsByUserAndNameAndLastNameAndDni(User user, String filters);

    List<StudentMonthlyFeeResponse> getStudentMonthlyFees(UUID studentId, String month, LocalDate expirationDate, MonthlyFeeStatus status);

    StudentMonthlyFeeResponse createStudentMonthlyFee(UUID studentId);
}
