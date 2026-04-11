package com.three_tech_solutions.slot_app.security.utils;

public class SecurityUtils {
    private SecurityUtils() {
        // Private constructor to prevent instantiation
    }

    public static String[] ENDPOINTS_WHITELIST = {
            "/docs",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/info"
    };

    public static String[] AUTHENTICATION_WHITELIST = {
            "/auth/sign-up",
            "/auth/restore-password",
            "/auth/restore-password/confirm",
            "/auth/restore-password/token/validate",
            "/ping"
    };


}
