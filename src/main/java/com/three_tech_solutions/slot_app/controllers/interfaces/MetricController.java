package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentsSummaryMetricsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/metrics")
public interface MetricController {

    @GetMapping("/student-payments")
    StudentPaymentsMetricResponse getStudentPaymentsMetric(@RequestParam UUID studentId);

    @GetMapping("/students-summary")
    StudentsSummaryMetricsResponse getStudentsSummaryMetrics();
}
