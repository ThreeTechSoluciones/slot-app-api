package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory.MonthlyFeeProcessorFactory;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.MonthlyFeeRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
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
public class MonthlyFeeServiceImpl implements MonthlyFeeService {

    private final MonthlyFeeRepository monthlyFeeRepository;
    private final StudentService studentService;
    private final MonthlyFeeProcessorFactory monthlyFeeProcessorFactory;

    @Transactional
    // @Scheduled(cron = "0 0 1 * * *")
    @Scheduled(cron = "* * * * * *")
    @Override
    public void createStudentsPayment() {
        log.info("Iniciando proceso de creacion de pagos");
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPlanType().getPaymentPlanName());
                MonthlyFee monthlyFee = monthlyFeeProcessor.createStudentPayment(student, getMonthlyFeeNumber());
                monthlyFeeRepository.save(monthlyFee);
            } catch (Exception e) {
                log.error("Hubo un error al crear el pago para el estudiante ", e);
            }
        });
    }

    @Override
    public MonthlyFee createInitialPayment(Student student, Byte extraClasses) {
        MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPlanType().getPaymentPlanName());
        MonthlyFee monthlyFee = monthlyFeeProcessor.createInitialStudentPayment(student, getMonthlyFeeNumber(), extraClasses);
        return monthlyFeeRepository.save(monthlyFee);
    }

    private int getMonthlyFeeNumber() {
        return monthlyFeeRepository.getLastMonthlyFeeNumber().orElse(1) + 1;
    }
}

