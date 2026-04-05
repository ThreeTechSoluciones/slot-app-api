package com.three_tech_solutions.slot_app.cron_jobs;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory.MonthlyFeeProcessorFactory;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CreateStudentsMonthlyFees {

    private final StudentService studentService;
    private final MonthlyFeeProcessorFactory monthlyFeeProcessorFactory;
    private final MonthlyFeeService monthlyFeeService;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void createStudentsMonthlyFee() {
        log.info("Iniciando proceso de creacion de pagos");
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
                Optional<MonthlyFee> monthlyFee = monthlyFeeProcessor.createStudentMonthlyFee(student, getMonthlyFeeNumber());
                monthlyFee
                        .ifPresentOrElse(
                                mf -> {
                                    monthlyFeeService.saveMonthlyFee(mf);
                                    log.info("Pago creado para el estudiante {}", student);
                                },
                                () -> log.info("No se creó pago para el estudiante {}", student)
                        );
            } catch (Exception e) {
                log.error("Hubo un error al crear el pago para el estudiante ", e);
            }
        });
    }

    private int getMonthlyFeeNumber() {
        return monthlyFeeService.getLastMonthlyFeeNumber();
    }


}
