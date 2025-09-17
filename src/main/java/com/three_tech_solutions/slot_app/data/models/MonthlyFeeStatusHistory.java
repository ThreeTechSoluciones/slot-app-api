package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class MonthlyFeeStatusHistory {
       LocalDateTime startDate;
       LocalDateTime endDate;
       PaymentStatus status;
       @Id
       UUID id;
}
