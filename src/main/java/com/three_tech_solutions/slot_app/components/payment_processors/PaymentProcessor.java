package com.three_tech_solutions.slot_app.components.payment_processors;

import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.utils.PricesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public abstract class PaymentProcessor {
    protected Payment createPayment(Student student, int newPaymentNumber, double amount) {
        return new Payment(
                amount,
                PaymentStatus.EN_TERMINO,
                getExpirationDate(student),
                newPaymentNumber,
                student
        );
    }

    public Payment createStudentPayment(Student student, int newPaymentNumber){
        log.info("Creando pago para el estudiante: {}", student);
        log.info("El plan es {}", getCurrentPlan());
        return createPayment(student, newPaymentNumber, getPaymentAmount(student));
    }
    public abstract Payment createInitialStudentPayment(Student student, int newPaymentNumber, Byte extraClasses);
    public abstract PlanType getCurrentPlan();

    public abstract  LocalDateTime getExpirationDate(Student student);
    private double getPaymentAmount(Student student) {
        byte studentClassesPerWeek = student.getPlan().getClassesPerWeek();
        List<Price> professorPrices = student.getUser().getPrices();

        Price selectedPrice = getProfessorPriceByStudentClassesPerWeek(professorPrices, studentClassesPerWeek);

        return studentClassesPerWeek * selectedPrice.getAmount();
    }

    private Price getProfessorPriceByStudentClassesPerWeek(List<Price> professorPrices, byte studentClassesPerWeek) {
        return professorPrices.stream().filter(price ->
                        price.getName().equals(getPriceNameByClasses(Integer.valueOf(studentClassesPerWeek)))
                )
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Hubo un error al obtener el precio"));
    }

    private String getPriceNameByClasses(Integer studentClassesPerWeek) {
        return PricesUtil.PriceNameByNumberOfDays.get(studentClassesPerWeek);
    }

}
