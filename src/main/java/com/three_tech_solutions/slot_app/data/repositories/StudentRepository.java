package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findAllByUserAndNameContainingAndLastnameContainingAndDniContaining(
            User user,
            String studentName,
            String studentLastname,
            String studentDni
    );
}
