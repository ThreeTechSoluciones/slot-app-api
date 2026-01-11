package com.three_tech_solutions.slot_app.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StudentSituation {
    EN_TERMINO("En término"),
    CON_DEUDA("Con deuda");

    final String name;

    StudentSituation(String name){
        this.name=name;
    }

    @JsonValue
    public String getName(){
        return name;
    }
}
