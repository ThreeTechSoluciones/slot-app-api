package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthday;
    private String pathologies;
    private LocalDate admissionDate;
    private boolean enabled;
    @OneToOne
    private Plan plan;
    @OneToMany
    @JoinColumn(name = "student_id")
    private List<Payment> payments;
    @Id
    private UUID id = UUID.randomUUID();

    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();
    private int createdBy;
    private int updatedBy;
    
}
