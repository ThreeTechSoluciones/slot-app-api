package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, UUID> {
}
