package com.three_tech_solutions.slot_app.components.monthly_fee_processors;

import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public abstract class MonthlyFeeProcessor {

    protected PaymentRepository paymentRepository;

    public MonthlyFeeProcessor(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

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
                getStudentPlanPrice(student),
                null
        );
    }

    /**
     * This method generates the first monthly fee when the student enrolls.
     * Unlike createStudentMonthlyFee(), the monthly fee payment is not null,
     * as the student must pay it upon starting classes.
     * @param student Student registered
     * @param newPaymentNumber Number of the new payment
     * @param createStudentRequest
     * @return The first monthly fee
     */
    public MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, CreateStudentRequest createStudentRequest) {
        double firstPaymentAmount = getFirstPaymentAmount(student, createStudentRequest);
        return createMonthlyFee(
                getExpirationDate(student),
                student,
                newPaymentNumber,
                firstPaymentAmount,
                buildPayment(firstPaymentAmount)
        );
    }

    protected MonthlyFee createMonthlyFee(
            LocalDateTime expirationDate,
            Student student,
            int newMonthlyFeeNumber,
            double amount,
            Payment payment
    ) {
        return new MonthlyFee(
                amount,
                expirationDate,
                newMonthlyFeeNumber,
                student,
                payment
        );
    }

    public abstract PaymentPlanName getCurrentPlan();

    public abstract LocalDateTime getExpirationDate(Student student);

    public abstract double getFirstPaymentAmount(Student student, CreateStudentRequest createStudentRequest);

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

    private Payment buildPayment(double firstPaymentAmount) {
        return new Payment(
                getPaymentNumber(),
                LocalDate.now(),
                firstPaymentAmount
        );
    }
    private Integer getPaymentNumber() {
        return getLastPaymentNumber() + 1;
    }

    private Integer getLastPaymentNumber() {
        return paymentRepository.getLastPaymentNumber().orElse(0);
    }
}
