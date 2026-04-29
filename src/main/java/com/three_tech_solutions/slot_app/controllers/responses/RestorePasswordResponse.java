package com.three_tech_solutions.slot_app.controllers.responses;

import com.three_tech_solutions.slot_app.utils.EmailUtils;

public record RestorePasswordResponse(
    String email

) {
    public RestorePasswordResponse(String email) {
        this.email = EmailUtils.ofuscateEmail(email);
    }
}
