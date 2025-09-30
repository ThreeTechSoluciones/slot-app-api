package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.data.models.Payment;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.data.repositories.PaymentRepository;
import com.three_tech_solutions.slot_app.services.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

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

    private int getNextPaymentNumber() {
        return paymentRepository.getLastPaymentNumber().orElse(0) + 1;
    }
}
