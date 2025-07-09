package com.three_tech_solutions.slot_app.data.models;

import jakarta.persistence.*;
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
public class Student {
    private String name;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthday;
    private String pathologies;
    private LocalDate admissionDate;
    private boolean enabled;
    @OneToOne(cascade = CascadeType.ALL)
    private Plan plan;
    @OneToMany
    private List<Payment> payments;
    @ManyToOne
    private User user;
    @Id
    private UUID id = UUID.randomUUID();

    public Student(String name, String lastname, String phoneNumber, LocalDate birthday, String pathologies, LocalDate admissionDate, boolean enabled, Plan plan, User user) {
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.pathologies = pathologies;
        this.admissionDate = admissionDate;
        this.enabled = enabled;
        this.plan = plan;
        this.user = user;
    }

    public Student(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

}

