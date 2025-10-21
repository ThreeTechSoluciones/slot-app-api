package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class MonthlyFee {
    double amount;
    LocalDate expirationDate;
    @Column(unique = true)
    int number;
    @ManyToOne
    Student student;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "monthly_fee_id")
    @OrderBy("startDate DESC")
    List<MonthlyFeeStatusHistory> statusHistory = new ArrayList<>(
            Collections.singletonList(
                    new MonthlyFeeStatusHistory(MonthlyFeeStatus.ON_TIME, LocalDateTime.now())
            )
    );
    @OneToOne(cascade = CascadeType.ALL)
    Payment payment = null;
    LocalDateTime createdAt = LocalDateTime.now();
    @Id
    UUID id = UUID.randomUUID();
    @Enumerated(EnumType.STRING)
    MonthlyFeeStatus currentStatus = MonthlyFeeStatus.ON_TIME;

    public MonthlyFee(
            double amount,
            LocalDate expirationDate,
            int number,
            Student student
    ) {
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.number = number;
        this.student = student;
    }

}
