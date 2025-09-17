package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class MonthlyFee {
    double amount;
    LocalDateTime expirationDate;
    @Column(unique = true)
    int number;
    @ManyToOne
    Student student;
    LocalDateTime createdAt = LocalDateTime.now();
    @Id
    UUID id = UUID.randomUUID();

    public MonthlyFee(double amount, LocalDateTime expirationDate, int number, Student student) {
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.number = number;
        this.student = student;
    }
}
