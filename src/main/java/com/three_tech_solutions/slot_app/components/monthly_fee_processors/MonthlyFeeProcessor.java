package com.three_tech_solutions.slot_app.components.monthly_fee_processors;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class MonthlyFeeProcessor {

    public MonthlyFee createStudentPayment(Student student, int newMonthlyFeeNumber){
        log.info("Creando pago para el estudiante: {}", student);
        log.info("El plan es {}", getCurrentPlan());
        return createPayment(
                getExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getStudentPlanPrice(student)
        );
    }

    protected MonthlyFee createPayment(LocalDateTime expirationDate, Student student, int newMonthlyFeeNumber, double amount) {
        return new MonthlyFee(
                amount,
                expirationDate,
                newMonthlyFeeNumber,
                student
        );
    }

    public abstract MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses);

    public abstract PaymentPlanName getCurrentPlan();

    public abstract  LocalDateTime getExpirationDate(Student student);

    protected double getStudentPlanPrice(Student student) {
        return student
                .getPlanType()
                .getPlan()
                .getPrices()
                .stream()
                .findFirst()
                .get()
                .getAmount();
    }

    protected PaymentPlanName getStudentPayentPlan(Student student) {
        return student
                .getPlanType()
                .getPaymentPlanName();
    }
}
