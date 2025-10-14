package com.three_tech_solutions.slot_app.components.monthly_fee_processors;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class MonthlyFeeProcessor {

    /**
     * This method generates the student's monthly fee. The payment is
     * null because monthly fees are generated for registration, and
     * payment must be made afterward
     * @param student Student to whom the payment should be generated
     * @param newMonthlyFeeNumber Number of the new monthly fee
     * @return MonthlyFee
     */
    public MonthlyFee createStudentMonthlyFee(Student student, int newMonthlyFeeNumber){
        log.info("Creando pago para el estudiante: {}", student);
        log.info("El plan es {}", getCurrentPlan());
        return createMonthlyFee(
                getExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getStudentPlanPrice(student)
        );
    }

    /**
     * This method generates the first monthly fee when the student enrolls.
     * Unlike createStudentMonthlyFee(), the amount depends on the payment type
     * selected by the student and the current date.
     * @param student Student registered
     * @param newMonthlyFeeNumber Number of the new payment
     * @param createStudentRequest
     * @return The first monthly fee
     */
    public MonthlyFee createInitialStudentPayment(Student student, int newMonthlyFeeNumber, CreateStudentRequest createStudentRequest) {
        return createMonthlyFee(
                getExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getFirstPaymentAmount(student, createStudentRequest)
        );
    }

    protected MonthlyFee createMonthlyFee(
            LocalDateTime expirationDate,
            Student student,
            int newMonthlyFeeNumber,
            double amount
    ) {
        return new MonthlyFee(
                amount,
                expirationDate,
                newMonthlyFeeNumber,
                student
        );
    }
    protected double getStudentPlanPrice(Student student) {
        return student
                .getPaymentPlan()
                .getPlan()
                .getCurrentPrice();
    }

    public abstract PaymentPlanName getCurrentPlan();

    public abstract LocalDateTime getExpirationDate(Student student);

    public abstract double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest);

}
