package com.three_tech_solutions.slot_app.controllers.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignInResponse(
        @Schema(description = "User ID")
        UUID userId,
        @Schema(description = "JWT to authenticate it with the server")
        String accessToken,
        @Schema(description = "JWT to refresh the access token when current one expires")
        String refreshToken
) {
}
