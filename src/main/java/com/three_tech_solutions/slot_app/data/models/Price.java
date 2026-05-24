package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Price {
    double amount;
    LocalDate startDate;
    LocalDate endDate = null;
    LocalDateTime createdAt = LocalDateTime.now();
    @Id
    UUID id = UUID.randomUUID();

    public Price(double amount, LocalDate startDate) {
        this.amount = amount;
        this.startDate = startDate;
    }

    public int getDaysUntilActive() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), this.startDate);
    }

    public boolean cantBeDeleted() {
        return LocalDate.now().isAfter(this.startDate) || LocalDate.now().isEqual(this.startDate) ;
    }
}
