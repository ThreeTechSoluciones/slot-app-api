package com.three_tech_solutions.slot_app.components.payment_processors;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class PaymentProcessor {

    public MonthlyFee createStudentPayment(Student student, int newPaymentNumber){
        log.info("Creando pago para el estudiante: {}", student);
        log.info("El plan es {}", getCurrentPlan());
        return createPayment(getExpirationDate(student),student, newPaymentNumber, getPaymentAmount(student));
    }

    protected MonthlyFee createPayment(LocalDateTime expirationDate, Student student, int newPaymentNumber, double amount) {
        return new MonthlyFee(
                amount,
                expirationDate,
                newPaymentNumber,
                student
        );
    }
    public abstract MonthlyFee createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses);

    public abstract PaymentPlanName getCurrentPlan();

    public abstract  LocalDateTime getExpirationDate(Student student);

    private double getPaymentAmount(Student student) {
        // TODO: Agregar monto del pago
        /*
        byte studentClassesPerWeek = student.getPlan().getClassesPerWeek();
        List<Price> professorPrices = student.getUser().getPrices();

        Price selectedPrice = getProfessorPriceByStudentClassesPerWeek(professorPrices, studentClassesPerWeek);

        return studentClassesPerWeek * selectedPrice.getAmount();

         */
        return 0.0;
    }
}
