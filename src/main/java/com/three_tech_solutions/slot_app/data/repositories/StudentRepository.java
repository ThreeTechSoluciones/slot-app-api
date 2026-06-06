package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
           AND (:isActive IS NULL OR s.enabled = :isActive)
           AND (:filterByAbsences = false OR EXISTS (
                   SELECT a FROM Absence a WHERE a.student = s AND a.status = 'PENDING'
           ))
           AND (
            :withDebt IS NULL
            OR (
                :withDebt = true
                AND EXISTS (
                    SELECT mf
                    FROM MonthlyFee mf
                    WHERE mf.student = s
                      AND mf.currentStatus = 'OUT_OF_TIME'
                )
            )
            OR (
                :withDebt = false
                AND NOT EXISTS (
                    SELECT mf
                    FROM MonthlyFee mf
                    WHERE mf.student = s
                      AND mf.currentStatus = 'OUT_OF_TIME'
                )
            )
        )
        ORDER BY s.enabled DESC
    """)
    Page<Student> getStudentsByUserAndFilters(
            @Param("user") User user,
            @Param("filter") String filter,
            @Param("filterByAbsences") boolean filterByAbsences,
            @Param("isActive") Boolean isActive,
            @Param("withDebt") Boolean withDebt,
            Pageable pageable
    );

    boolean existsByDni(String dni);

    List<Student> findByPaymentPlan_PaymentPlanName(PaymentPlanName paymentPlanName);
}
