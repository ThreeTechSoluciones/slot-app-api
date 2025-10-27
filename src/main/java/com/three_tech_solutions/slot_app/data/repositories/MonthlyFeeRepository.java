package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonthlyFeeRepository extends JpaRepository<MonthlyFee, UUID> {
    @Query("SELECT number FROM MonthlyFee ORDER BY number DESC LIMIT 1")
    Optional<Integer> getLastMonthlyFeeNumber();

    @Query("""
        SELECT mf
        FROM MonthlyFee mf
        WHERE mf.student = :student
            AND (:month IS NULL OR MONTH(mf.expirationDate) = :month)
            AND (:status IS NULL OR mf.currentStatus = :status)
            AND (:expirationDate IS NULL OR mf.expirationDate = :expirationDate)
        ORDER BY mf.number DESC
    """)
    List<MonthlyFee> findAllByStudentAndMonthAndStatusAndExpirationDate(
            @Param("student") Student student,
            @Param("month") Integer month,
            @Param("status") MonthlyFeeStatus status,
            @Param("expirationDate") LocalDate expirationDate
    );
}
