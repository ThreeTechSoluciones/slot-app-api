package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class MonthlyFeeStatusHistory {
    @Enumerated(EnumType.STRING)
    MonthlyFeeStatus status;
    LocalDateTime startDate;
    LocalDateTime endDate = null;
    @Id
    UUID id = UUID.randomUUID();

    public MonthlyFeeStatusHistory(MonthlyFeeStatus status, LocalDateTime startDate) {
        this.status = status;
        this.startDate = startDate;
    }
}
