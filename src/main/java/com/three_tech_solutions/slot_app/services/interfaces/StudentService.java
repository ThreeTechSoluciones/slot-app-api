package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.StudentRequestDTO;
import com.three_tech_solutions.slot_app.dto.StudentResponseDTO;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO studentDTO);

}
