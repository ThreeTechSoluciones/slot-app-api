package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<MonthlyFee, UUID> {
    @Query("SELECT number FROM Payment ORDER BY number DESC LIMIT 1")
    Optional<Integer> getLastPaymentNumber();
}
