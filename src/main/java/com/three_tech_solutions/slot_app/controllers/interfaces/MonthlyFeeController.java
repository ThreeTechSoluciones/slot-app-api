package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.MonthlyFeePaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/monthly-fees")
public interface MonthlyFeeController {
    @PostMapping("/{monthlyFeeId}/pay")
    @ResponseStatus(HttpStatus.OK)
    MonthlyFeePaymentResponse payMonthlyFee(@PathVariable UUID monthlyFeeId);
}
