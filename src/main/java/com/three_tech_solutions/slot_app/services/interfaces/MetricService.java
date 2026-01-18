package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.StudentPaymentsMetricResponse;

import java.util.UUID;

public interface MetricService {
    StudentPaymentsMetricResponse getStudentPaymentsMetric(UUID studentId);
}
