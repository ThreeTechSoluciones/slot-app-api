package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Student {
     String name;
     String lastname;
     @Column(unique = true)
     String dni;
     String phoneNumber;
     LocalDate birthday;
     String pathologies;
     @ManyToOne
     User user;
     @OneToOne(cascade = CascadeType.ALL)
     PaymentPlan paymentPlan;
     boolean enabled = true;
     LocalDate admissionDate = LocalDate.now();
     @OneToMany
     @JoinColumn(name = "student_id")
     List<MonthlyFee> monthlyFees = Collections.emptyList();
     @OneToMany
     @JoinColumn(name = "student_Id")
     List<Payment> payments = Collections.emptyList();
     @Id
     UUID id = UUID.randomUUID();

    public Student(String name, String lastname, String dni, String phoneNumber, LocalDate birthday, String pathologies, User user, PaymentPlan paymentPlan) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.pathologies = pathologies;
        this.user = user;
        this.paymentPlan = paymentPlan;
    }

    public StudentSituation getStudentSituation() {
        return studentHasAnyPaymentExpired() ?
                StudentSituation.CON_DEUDA :
                StudentSituation.EN_TERMINO;
    }

    private boolean studentHasAnyPaymentExpired() {
        return this
                .getMonthlyFees()
                .stream()
                .anyMatch(monthlyFee -> monthlyFee.getCurrentStatus() == MonthlyFeeStatus.OUT_OF_TIME);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dni='" + dni + '\'' +
                '}';
    }
}

