package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query("""
        SELECT s
        FROM Student s
        WHERE s.user = :user
           AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :filter, '%'))
           OR LOWER(s.lastname) LIKE LOWER(CONCAT('%', :filter, '%'))
           OR LOWER(CONCAT(s.name, ' ', s.lastname)) LIKE LOWER(CONCAT('%', :filter, '%'))
           OR LOWER(CONCAT(s.lastname, ' ', s.name)) LIKE LOWER(CONCAT('%', :filter, '%'))
           OR s.dni LIKE CONCAT(:filter, '%'))
    """)
    Page<Student> getStudentsByUserAndNameAndLastnameAndDni(@Param("user") User user, @Param("filter") String filter, Pageable pageableWithSort );

    boolean existsByDni(String dni);
}
