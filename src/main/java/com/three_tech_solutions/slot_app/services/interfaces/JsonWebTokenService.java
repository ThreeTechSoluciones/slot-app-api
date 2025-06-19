package com.three_tech_solutions.slot_app.services.interfaces;

public interface JsonWebTokenService {
    String getRefreshToken(String subject);
    String getAccessToken(String subject);
    String getSubject(String jwt);
    boolean isValid(String jwt);
}
