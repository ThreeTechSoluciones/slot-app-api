package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.StudentSituation;

import java.util.UUID;

public record StudentResponse (
    String name,
    String lastname,
    String dni,
    StudentSituation status,
    Boolean isActive,
    UUID id

) {}
