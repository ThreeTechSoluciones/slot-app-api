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
public class BeginningOfMonthPaymentProcessor extends PaymentProcessor {

    private final int BEGINNING_OF_MONTH_EXPIRATION_DATE=10;

    @Override
    public PlanType getCurrentPlan() {
        return PlanType.BEGINNING_OF_MONTH;
    }

    @Override
    public LocalDateTime getExpirationDate(Student student) {
        return LocalDateTime.now().withDayOfMonth(BEGINNING_OF_MONTH_EXPIRATION_DATE);
    }

    @Override
    public Payment createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses) {
        return getTodayDay() <= 10 ?
                createStudentPayment(student, newPaymentNumber) :
                createPaymentWithExtraClasses(student, newPaymentNumber, extraClasses);
    }

    private static int getTodayDay() {
        return LocalDate.now().getDayOfMonth();
    }

    private Payment createPaymentWithExtraClasses(Student student, int newPaymentNumber, Byte extraClasses) {
        return createPayment(student, newPaymentNumber, calculateExtraClassesAmount(student, extraClasses));
    }

    private double calculateExtraClassesAmount(Student student, Byte extraClasses) {
        validateExtraClassesOrThrowException(extraClasses);
        double pricePerClass = student.getUser().getPrices().stream()
                .filter(p -> p.getName().equals("Clase"))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No se encontró precio por clase"))
                .getAmount();

        return extraClasses * pricePerClass;
    }

    private static void validateExtraClassesOrThrowException(Byte extraClasses) {
        if (extraClasses == null || extraClasses <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar la cantidad de clases extra si la inscripción es posterior al día 10");
        }
    }
}
