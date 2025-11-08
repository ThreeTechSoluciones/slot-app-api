package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.responses.PaymentDetailsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentDetailsResponse;
import com.three_tech_solutions.slot_app.data.mappers.PaymentMapper;
import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final @Lazy MonthlyFeeService monthlyFeeService;

    @Override
    @Transactional
    public Payment createPayment(Student student, double amount) {
        Payment payment = new Payment(
                getNextPaymentNumber(),
                LocalDate.now(),
                amount,
                student
        );
        return paymentRepository.save(payment);
    }
    @Override
    public PaymentDetailsResponse getPaymentDetails(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        int monthlyFeeNumber = monthlyFeeService.findAssociatedMonthlyFeeNumber(payment);
        return PaymentMapper.toPaymentDetailsResponse(payment, monthlyFeeNumber);
    }

    private int getNextPaymentNumber() {
        return paymentRepository.getLastPaymentNumber().orElse(0) + 1;
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El pago no existe"));
    }
}