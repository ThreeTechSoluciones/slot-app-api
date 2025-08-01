package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.util.List;

public class StudentMapper {

        public static StudentResponse toResponse(Student student) {
            return new StudentResponse(
                    student.getName(),
                    student.getLastname(),
                    student.getDni(),
                    student.isEnabled(),
                    student.getId()
            );
        }

        public static List<StudentResponse> toResponseList(List<Student> students) {
            return students.stream()
                    .map(StudentMapper::toResponse)
                    .toList();
        }
    }

