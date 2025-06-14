package com.three_tech_solutions.SlottApp.data.repositories;

import com.three_tech_solutions.SlottApp.data.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
