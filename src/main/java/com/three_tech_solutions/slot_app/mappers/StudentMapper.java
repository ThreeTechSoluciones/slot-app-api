package com.three_tech_solutions.slot_app.mappers;

import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.dto.StudentRequestDTO;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.StudentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    /**
     * Mapea un StudentRequestDTO a la entidad Student.
     * El Plan y los Payments se reciben como parámetros, ya deben ser buscados o generados en el Service.
     */

    public Student toStudent(StudentRequestDTO studentDTO, Plan plan, User user) {
        Student student = new Student();

        student.setName(studentDTO.getName());
        student.setLastname(studentDTO.getLastName());
        student.setPhoneNumber(studentDTO.getCellphoneNumber());
        student.setBirthday(studentDTO.getBirthday());
        student.setAdmissionDate(studentDTO.getAdmissionDate());
        student.setPathologies(studentDTO.getPathologies());
        student.setEnabled(true);// Activo por defecto
        student.setPlan(plan);
        student.setUser(user);

        return student;
    }

    public StudentResponseDTO toStudentDTO(Student student) {
        StudentResponseDTO studentDTO = new StudentResponseDTO();
        studentDTO.setName(student.getName());
        studentDTO.setLastName(student.getLastname());

        return studentDTO;
    }


}
