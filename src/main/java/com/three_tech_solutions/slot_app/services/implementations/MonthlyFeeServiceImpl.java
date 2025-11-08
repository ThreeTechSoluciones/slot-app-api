package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory.MonthlyFeeProcessorFactory;
import com.three_tech_solutions.slot_app.controllers.requests.CreateStudentRequest;
import com.three_tech_solutions.slot_app.controllers.responses.StudentMonthlyFeeResponse;
import com.three_tech_solutions.slot_app.data.enums.MonthlyFeeStatus;
import com.three_tech_solutions.slot_app.data.mappers.MonthlyFeeMapper;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.MonthlyFeeStatusHistory;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.MonthlyFeeRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import com.three_tech_solutions.slot_app.services.interfaces.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class MonthlyFeeServiceImpl implements MonthlyFeeService {

    private final MonthlyFeeRepository monthlyFeeRepository;
    private final StudentService studentService;
    private final MonthlyFeeProcessorFactory monthlyFeeProcessorFactory;
    private final PaymentService paymentService;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    @Override
    public void createStudentsMonthlyFee() {
        log.info("Iniciando proceso de creacion de pagos");
        List<Student> students = studentService.getStudents();
        students.forEach(student -> {
            try {
                MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
                Optional<MonthlyFee> monthlyFee = monthlyFeeProcessor.createStudentMonthlyFee(student, getMonthlyFeeNumber());
                monthlyFee
                        .ifPresentOrElse(
                                monthlyFeeRepository::save,
                                () -> log.info("No se creó pago para el estudiante {}", student)
                        );
            } catch (Exception e) {
                log.error("Hubo un error al crear el pago para el estudiante ", e);
            }
        });
    }

    @Override
    public void createInitialMonthlyFee(Student student, CreateStudentRequest createStudentRequest) {
        MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
        MonthlyFee monthlyFee = monthlyFeeProcessor.createInitialStudentPayment(
                student,
                getMonthlyFeeNumber(),
                createStudentRequest
        );
        monthlyFeeRepository.save(monthlyFee);
    }

    @Override
    public void payMonthlyFee(UUID monthlyFeeId) {
        MonthlyFee monthlyFee = getMonthlyFeeById(monthlyFeeId);

        validateNotAlreadyPaid(monthlyFee);

        Payment payment = paymentService.createPayment(monthlyFee.getStudent(), monthlyFee.getAmount());

        monthlyFee.setPayment(payment);

        updateStatus(monthlyFee);

        monthlyFeeRepository.save(monthlyFee);
    }

    @Override
    public List<StudentMonthlyFeeResponse> getMonthlyFeesByStudent(Student student, String month, LocalDate expirationDate, MonthlyFeeStatus status) {
        return monthlyFeeRepository
                .findAllByStudentAndMonthAndStatusAndExpirationDate(student, getMonthValue(month), status, expirationDate)
                .stream()
                .map(MonthlyFeeMapper::toStudentMonthlyFeeResponse)
                .toList();
    }

    @Override
    public int findAssociatedMonthlyFeeNumber(Payment payment) {
        MonthlyFee monthlyFee = monthlyFeeRepository.findByPayment(payment)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la cuota asociada al pago"));
        return monthlyFee.getNumber();
    }

    private static Integer getMonthValue(String month) {
        return Optional
                .ofNullable(month)
                .map(m -> Month.valueOf(m.toUpperCase()).getValue())
                .orElse(null);
    }

    private int getMonthlyFeeNumber() {
        return monthlyFeeRepository.getLastMonthlyFeeNumber().orElse(0) + 1;
    }

    private MonthlyFee getMonthlyFeeById(UUID monthlyFeeId) {
        return monthlyFeeRepository.findById(monthlyFeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuota no existe"));
    }

    private void validateNotAlreadyPaid(MonthlyFee monthlyFee) {
        if (monthlyFee.getPayment() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuota ya está pagada");
        }
    }

    private void updateStatus(MonthlyFee monthlyFee) {
        monthlyFee.getStatusHistory().stream()
                .filter(h -> h.getEndDate() == null)
                .findFirst()
                .ifPresent(h -> h.setEndDate(LocalDateTime.now()));

        MonthlyFeeStatus newStatus = LocalDate.now().isAfter(monthlyFee.getExpirationDate())
                ? MonthlyFeeStatus.PAYED_OUT_OF_TIME
                : MonthlyFeeStatus.PAYED;

        monthlyFee.setCurrentStatus(newStatus);

        MonthlyFeeStatusHistory newStatusHistory = new MonthlyFeeStatusHistory(newStatus, LocalDateTime.now());
        monthlyFee.getStatusHistory().add(newStatusHistory);
    }


}
