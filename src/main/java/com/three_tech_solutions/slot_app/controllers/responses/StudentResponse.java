package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.data.enums.StudentSituation;

import static com.three_tech_solutions.slot_app.data.enums.StudentSituation.EN_TERMINO;

public record StudentResponse (
    String name,
    String lastname,
    StudentSituation status,
    Boolean isActive

) {
    public StudentResponse(String name, String lastname, boolean isActive) {
        this (name, lastname, EN_TERMINO, isActive);
    }
}
