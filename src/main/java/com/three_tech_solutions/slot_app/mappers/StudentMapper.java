package com.three_tech_solutions.slot_app.mappers;

import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.dto.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.StudentResponse;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    /**
     * Mapea un StudentRequestDTO a la entidad Student.
     * El Plan y los Payments se reciben como parámetros, ya deben ser buscados o generados en el Service.
     */

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


}
