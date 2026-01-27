package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentsSummaryMetricsResponse;

import java.util.UUID;

public interface MetricService {
    StudentPaymentsMetricResponse getStudentPaymentsMetric(UUID studentId);

    StudentsSummaryMetricsResponse getStudentsSummaryMetrics();
}
