package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.payment_processors.PaymentProcessor;
import com.three_tech_solutions.slot_app.components.payment_processors.factory.PaymentProcessorFactory;
import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentService studentService;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    @Override
    public void createStudentsPayment() {
        log.info("Iniciando proceso de creacion de pagos");
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                // TODO: Obtener el plan del estudiante
                PaymentProcessor paymentProcessor = paymentProcessorFactory.getPaymentProcessor(PaymentPlanName.SPECIFIC_DAY);
                MonthlyFee monthlyFee = paymentProcessor.createStudentPayment(student, getPaymentNumber());
                paymentRepository.save(monthlyFee);
            } catch (Exception e) {
                log.error("Hubo un error al crear el pago para el estudiante ", e);
            }
        });
    }

    @Override
    public MonthlyFee createInitialPayment(Student student, Byte extraClasses) {
        // TODO: Obtener el plan del estudiante
        PaymentProcessor paymentProcessor = paymentProcessorFactory.getPaymentProcessor(PaymentPlanName.SPECIFIC_DAY);
        MonthlyFee monthlyFee = paymentProcessor.createInitialStudentPayment(student, getPaymentNumber(), extraClasses);
        return paymentRepository.save(monthlyFee);
    }

    private int getPaymentNumber() {
        return paymentRepository.getLastPaymentNumber().orElse(0) + 1;
    }
}

