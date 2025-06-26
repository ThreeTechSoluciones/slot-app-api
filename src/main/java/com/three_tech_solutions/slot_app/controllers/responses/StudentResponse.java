package com.three_tech_solutions.slot_app.controllers.responses;

public class StudentResponse {
    private String name;
    private String lastname;
    private String status = "En término";

    public StudentResponse(String name, String lastname) {
        this.name=name;
        this.lastname=lastname;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getStatus() {
        return status;
    }
}
