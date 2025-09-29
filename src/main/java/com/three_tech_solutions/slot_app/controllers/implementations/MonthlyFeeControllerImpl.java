package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.MonthlyFeeController;
import com.three_tech_solutions.slot_app.services.interfaces.MonthlyFeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class MonthlyFeeControllerImpl implements MonthlyFeeController {
    private final MonthlyFeeService monthlyFeeService;

    @Override
    public void payMonthlyFee(UUID monthlyFeeId) {
        monthlyFeeService.payMonthlyFee(monthlyFeeId);
    }
}
