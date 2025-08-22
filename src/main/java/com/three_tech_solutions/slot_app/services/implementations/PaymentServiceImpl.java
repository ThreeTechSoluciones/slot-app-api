package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.enums.PaymentStatus;
import com.three_tech_solutions.slot_app.data.enums.PlanType;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import com.three_tech_solutions.slot_app.utils.PricesUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentService studentService;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    @Override
    public void createStudentsPayment() {
        log.info("Iniciando proceso de creacion de pagos");
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                log.info("Creando pago para el estudiante: {}", student);
                if (studentPlanIsPrincipioDeMes(student)) {
                    log.info("El plan es {}", PlanType.PRINCIPIO_DE_MES.getName());
                    paymentRepository.save(new Payment(
                            getPaymentAmount(student),
                            PaymentStatus.EN_TERMINO,
                            LocalDateTime.now().withDayOfMonth(10),
                            getPaymentNumber(),
                            student
                    ));
                } else{
                    log.info("El plan es {}", PlanType.DIA_ESPECIFICO.getName());
                    paymentRepository.save(new Payment(
                            getPaymentAmount(student),
                            PaymentStatus.EN_TERMINO,
                            LocalDateTime.now().withDayOfMonth(student.getPlan().getPaymentDay()),
                            getPaymentNumber(),
                            student
                    ));
                }
            } catch (DateTimeException e) {
                log.error("El día de pago no es válido");
            } catch (Exception e) {
                log.error("Hubo un error al crear el pago para el estudiante ", e);
            }

        });
    }

    private static boolean studentPlanIsPrincipioDeMes(Student student) {
        return student.getPlan().getPlanType().equals(PlanType.PRINCIPIO_DE_MES);
    }

    private int getPaymentNumber() {
        return paymentRepository.getLastPaymentNumber().orElse(0) + 1;
    }

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

