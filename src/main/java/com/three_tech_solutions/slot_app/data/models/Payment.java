package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private LocalDateTime paymentDate = null;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime expirationDate;
    @Column(unique = true)
    private int number;
    @Id
    private UUID id = UUID.randomUUID();

    public Payment(double amount, PaymentStatus status, LocalDateTime expirationDate, int number) {
        this.amount = amount;
        this.status = status;
        this.expirationDate = expirationDate;
        this.number = number;
    }
}
