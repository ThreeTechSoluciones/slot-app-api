package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.util.List;
import java.util.stream.Collectors;

public class MapperStudent {

        public static StudentResponse toResponse(Student student) {
            return new StudentResponse(
                    student.getName(),
                    student.getLastname()
            );
        }

        public static List<StudentResponse> toResponseList(List<Student> students) {
            return students.stream()
                    .map(MapperStudent::toResponse)
                    .toList();
        }
    }

