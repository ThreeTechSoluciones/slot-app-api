package com.three_tech_solutions.slot_app.controllers.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.controllers.responses.RestorePasswordResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.exceptions.responses.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RequestMapping("/auth")
public interface AuthController {
    @Operation(
            summary = "Sign in with an registered user",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has logged in correctly",
                    content = { @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignInResponse.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Credentials are wrong",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            )
    })
    @PostMapping("/sign-in")
    SignInResponse signIn(
            @Parameter(
                    name = "Authorization",
                    description = "Authorization header with format 'Basic base64(username:password)'",
                    in = ParameterIn.HEADER,
                    required = true,
                    example = "Basic dXNlcm5hbWU6cGFzc3dvcmQ="
            )
            @RequestHeader(name = "authorization")
            String authorization
    );

    @PostMapping("/sign-up")
    void signUp(
            @RequestBody
            CreateUserRequest createUserRequest
    );

    @PostMapping("/restore-password")
    @ResponseStatus(HttpStatus.OK)
    RestorePasswordResponse restorePassword(@Valid @RequestBody RestorePasswordRequest restorePasswordRequest);

    @PostMapping("/restore-password/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void recoverPassword(@Valid @RequestBody RecoverPasswordRequest recoverPasswordRequest);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/restore-password/token/validate")
    void validateTokenAndDisableIt(@RequestBody ValidateTokenRequest request);
}
