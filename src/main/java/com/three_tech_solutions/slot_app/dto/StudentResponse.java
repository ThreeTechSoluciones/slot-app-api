package com.three_tech_solutions.slot_app.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentResponse {
    private String name;
    private String lastName;
    private UUID studentId;
}
