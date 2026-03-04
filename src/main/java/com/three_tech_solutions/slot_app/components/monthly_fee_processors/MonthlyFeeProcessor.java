package com.three_tech_solutions.slot_app.components.monthly_fee_processors;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Optional;

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
    public Optional<MonthlyFee> createStudentMonthlyFee(Student student, int newMonthlyFeeNumber){
        log.info("Creando pago para el estudiante: {}", student);
        log.info("El plan es {}", getCurrentPlan());
        return shouldCreateStudentMonthlyFee(student) ? getOptionalOfMonthlyFee(student, newMonthlyFeeNumber) : Optional.empty();
    }

    /**
     * This method generates the first monthly fee when the student enrolls.
     * Unlike createStudentMonthlyFee(), the amount depends on the payment type
     * selected by the student and the current date.
     * @param student Student registered
     * @param newMonthlyFeeNumber Number of the new payment
     * @param initialPaymentContext
     * @return The first monthly fee
     */
    public MonthlyFee createInitialStudentPayment(Student student, int newMonthlyFeeNumber, InitialPaymentContext initialPaymentContext) {
        return createMonthlyFee(
                getExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getFirstPaymentAmount(student, initialPaymentContext)
        );
    }


    public Optional<MonthlyFee> createNextStudentMonthlyFee(Student student, int newMonthlyFeeNumber) {
        return Optional.of(createMonthlyFee(
                getNextExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getStudentPlanPrice(student)
        ));
    }

    protected int getTodayDay() {
        return LocalDate.now().getDayOfMonth();
    }

    protected MonthlyFee createMonthlyFee(
            LocalDate expirationDate,
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

    public abstract PaymentPlanName getCurrentPlan();

    public abstract LocalDate getNextExpirationDate(Student student);

    public abstract LocalDate getExpirationDate(Student student);

    public abstract double getFirstPaymentAmount(Student student, InitialPaymentContext initialPaymentContext);

    public abstract boolean studentHasTheCurrentMonthlyFee(Student student);

    protected double getStudentPlanPrice(Student student) {
        return student
                .getPaymentPlan()
                .getPlan()
                .getCurrentPrice()
                .getAmount();
    }

    private Optional<MonthlyFee> getOptionalOfMonthlyFee(Student student, int newMonthlyFeeNumber) {
        return Optional.of(createMonthlyFee(
                getExpirationDate(student),
                student,
                newMonthlyFeeNumber,
                getStudentPlanPrice(student)
        ));
    }

    private boolean shouldCreateStudentMonthlyFee(Student student) {
        return !studentHasTheCurrentMonthlyFee(student);
    }
}
