package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.monthly_fee_processors.MonthlyFeeProcessor;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.context.InitialPaymentContext;
import com.three_tech_solutions.slot_app.components.monthly_fee_processors.factory.MonthlyFeeProcessorFactory;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class MonthlyFeeServiceImpl implements MonthlyFeeService {

    private final MonthlyFeeRepository monthlyFeeRepository;
    private final MonthlyFeeProcessorFactory monthlyFeeProcessorFactory;
    private final PaymentService paymentService;

    @Override
    public void createInitialMonthlyFee(Student student, InitialPaymentContext initialPaymentContext) {
        MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
        MonthlyFee monthlyFee = monthlyFeeProcessor.createInitialStudentPayment(
                student,
                getLastMonthlyFeeNumber(),
                initialPaymentContext
        );
        saveMonthlyFee(monthlyFee);
    }

    @Override
    public void payMonthlyFee(UUID monthlyFeeId) {
        MonthlyFee monthlyFee = getMonthlyFeeById(monthlyFeeId);

        validateNotAlreadyPaid(monthlyFee);

        Payment payment = paymentService.createPayment(monthlyFee.getStudent(), monthlyFee.getAmount());

        monthlyFee.setPayment(payment);

        updateStatus(monthlyFee);

        saveMonthlyFee(monthlyFee);
    }

    @Override
    public Page<StudentMonthlyFeeResponse> getMonthlyFeesByStudent(Student student, String month, LocalDate expirationDate, MonthlyFeeStatus status, Pageable pageable) {
        return monthlyFeeRepository
                .findAllByStudentAndMonthAndStatusAndExpirationDate(student, getMonthValue(month), status, expirationDate, pageable)
                .map(MonthlyFeeMapper::toStudentMonthlyFeeResponse);
    }

    @Override
    public StudentMonthlyFeeResponse createMonthlyFeeForStudent(Student student) {
        MonthlyFeeProcessor monthlyFeeProcessor = monthlyFeeProcessorFactory.getPaymentProcessor(student.getPaymentPlan().getPaymentPlanName());
        Optional<MonthlyFee> monthlyFee = monthlyFeeProcessor.createNextStudentMonthlyFee(student, getLastMonthlyFeeNumber());
        MonthlyFee savedMonthlyFee = monthlyFee
                .map(this::saveMonthlyFee)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la cuota para el estudiante"));
        return MonthlyFeeMapper.toStudentMonthlyFeeResponse(savedMonthlyFee);
    }

    @Override
    public int findAssociatedMonthlyFeeNumber(Payment payment) {
        MonthlyFee monthlyFee = monthlyFeeRepository.findByPayment(payment)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la cuota asociada al pago"));
        return monthlyFee.getNumber();
    }

    @Override
    public MonthlyFee getMonthlyFeeById(UUID monthlyFeeId) {
        return monthlyFeeRepository.findById(monthlyFeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuota no existe"));
    }

    @Override
    public void deleteMonthlyFee(MonthlyFee monthlyFee) {
        monthlyFeeRepository.delete(monthlyFee);
    }

    @Override
    public int getLastMonthlyFeeNumber() {
        return monthlyFeeRepository.getLastMonthlyFeeNumber().orElse(0) + 1;
    }

    @Override
    public MonthlyFee saveMonthlyFee(MonthlyFee monthlyFee) {
        return monthlyFeeRepository.save(monthlyFee);
    }

    private static Integer getMonthValue(String month) {
        return Optional
                .ofNullable(month)
                .map(m -> Month.valueOf(m.toUpperCase()).getValue())
                .orElse(null);
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
