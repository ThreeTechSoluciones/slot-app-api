package com.three_tech_solutions.slot_app.data.repositories;

import com.three_tech_solutions.slot_app.data.models.Notification;
import com.three_tech_solutions.slot_app.data.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
