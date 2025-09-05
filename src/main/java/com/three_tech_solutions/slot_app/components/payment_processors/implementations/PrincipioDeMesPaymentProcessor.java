package com.three_tech_solutions.slot_app.components.payment_processors.implementations;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PrincipioDeMesPaymentProcessor extends PaymentProcessor {

    @Override
    public PlanType getCurrentPlan() {
        return PlanType.PRINCIPIO_DE_MES;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(10);
    }

    @Override
    public Payment createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        LocalDate today = LocalDate.now();

        if (today.getDayOfMonth() <= 10) {
            return createStudentPayment(student, newPaymentNumber);
        }
        double amount = calculateExtraClassesAmount(student, extraClasses);
        return new Payment(amount, PaymentStatus.EN_TERMINO, getExpirationDate(student), newPaymentNumber, student);
    }

    private double calculateExtraClassesAmount(Student student, Byte extraClasses) {
        if (extraClasses == null || extraClasses <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar la cantidad de clases extra si la inscripción es posterior al día 10");
        }

        double pricePerClass = student.getUser().getPrices().stream()
                .filter(p -> p.getName().equals("Clase"))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se encontró precio por clase"))
                .getAmount();

        return extraClasses * pricePerClass;
    }
}
