package com.three_tech_solutions.slot_app.utils;

import java.util.Base64;

public class AuthUtils {
    private static String BASIC_PREFIX = "Basic ";
    private static String COLON_SYMBOL = ":";
    public static final String BEARER_PREFIX = "Bearer ";

    private AuthUtils() {}

    public static String extractUsername(String authorization) {
        return getCredentials(authorization)[0];
    }

    private static String[] getCredentials(String authorization) {
        return decodeAuthorizationHeader(authorization).split(COLON_SYMBOL);
    }

    private static String decodeAuthorizationHeader(String authorization) {
        return new String(
                decodeString(
                        authorization
                                .substring(BASIC_PREFIX.length())
                )
        );
    }

    public static String removeBearerPrefix(String jwtToken) {
        return jwtToken.substring(BEARER_PREFIX.length());
    }

    private static byte[] decodeString(String encodedCredentials) {
        return Base64.getDecoder().decode(encodedCredentials);
    }
}
