package com.three_tech_solutions.slot_app.dto;

public class StudentUpdateResponse {
    private String name;
    private String lastName;
    private String dni;
    private String phoneNumber;
    private LocalDate birthday;
    private LocalDate admissionDate;
    private String pathologies;
    private boolean enabled;
    private UUID studentId;
    private PlanType planType;
    private int classesPerWeek;
    private int paymentDay;
    private StudentSituation situation;
}
