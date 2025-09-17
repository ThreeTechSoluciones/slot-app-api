package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Payment {
    int number;
    LocalDate paymentDate;
    double amount;
    @Id
    UUID id = UUID.randomUUID();

    public Payment(int number, LocalDate paymentDate, double amount) {
        this.number = number;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }
}
