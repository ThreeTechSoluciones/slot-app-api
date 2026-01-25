package com.three_tech_solutions.slot_app.controllers.implementations;

import com.three_tech_solutions.slot_app.controllers.interfaces.MetricController;
import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;
import com.three_tech_solutions.slot_app.services.interfaces.MetricService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class MetricControllerImpl implements MetricController {

    private final MetricService metricService;

    @Override
    public StudentPaymentsMetricResponse getStudentPaymentsMetric(UUID studentId) {
        return metricService.getStudentPaymentsMetric(studentId);
    }
}
