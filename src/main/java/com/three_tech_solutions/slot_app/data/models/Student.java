package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.AbsenceStatus;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.enums.StudentSituation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
     private String name;
     private String lastname;
     @Column(unique = true)
     String dni;
     String phoneNumber;
     LocalDate birthday;
     @Column(length = 300)
     String pathologies;
     @ManyToOne
     private User user;
     @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
     private PaymentPlan paymentPlan;
     private boolean enabled = true;
     private LocalDate admissionDate = LocalDate.now();
     @OneToMany
     @JoinColumn(name = "student_id")
     private List<MonthlyFee> monthlyFees = Collections.emptyList();
     @OneToMany
     @JoinColumn(name = "student_id")
     private List<Payment> payments = Collections.emptyList();
     @OneToMany(cascade = CascadeType.ALL)
     @JoinColumn(name = "student_id")
     private List<Absence> absences = Collections.emptyList();
     @Id
     private UUID id = UUID.randomUUID();

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

    public Optional<MonthlyFee> getLatestMonthlyFee() {
        return this
                .getMonthlyFees()
                .stream()
                .max(Comparator.comparingInt(MonthlyFee::getNumber));
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

    public void registerAbsenceAsRecovered() {
        this.getAbsences()
                .stream()
                .filter(absence -> absence.getStatus() == AbsenceStatus.PENDING)
                .min(Comparator.comparing(absence -> absence.getSlotDate().isBefore(absence.getSlotDate())))
                .ifPresentOrElse(
                        absence -> absence.setStatus(AbsenceStatus.RECOVERED),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estudiante no tiene turnos para recuperar.");
                        }
                );
    }
}

