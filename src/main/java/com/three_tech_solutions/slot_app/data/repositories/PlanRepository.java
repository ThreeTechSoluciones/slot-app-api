package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Plan;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByIdOrderByPrices_createdAtDesc(UUID planId);

    @Query("""
        SELECT p
        FROM Plan p
        WHERE p.user = :user
           AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :planName, '%')))
    """)
    Page<Plan> findAllByUserAndPlanName(@Param("user") User user, @Param("planName") String planName, Pageable pageable);
}
