package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.dto.StudentResponseDTO;
import com.three_tech_solutions.slot_app.controllers.interfaces.StudentController;
import com.three_tech_solutions.slot_app.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.dto.StudentRequestDTO;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentControllerImpl implements StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentControllerImpl(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
    }

    @Override
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO studentDTO) {
        //conversión studentDTO a student, DTO a entidad.
        Student student = studentMapper.toStudent(studentDTO);
        //Llamamos al servicio con la entidad, student.
        StudentResponseDTO response =  studentService.createStudent(student);
        //conversion student a studentDTO, entidad a DTO.
        StudentResponseDTO responseDTO = studentMapper.toStudentDTO(response); //DTO a entidades
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
