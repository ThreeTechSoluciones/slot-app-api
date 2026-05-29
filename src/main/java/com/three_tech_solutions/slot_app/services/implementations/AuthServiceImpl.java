package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.ValidateTokenRequest;
import com.three_tech_solutions.slot_app.controllers.responses.RestorePasswordResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@AllArgsConstructor
class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JsonWebTokenService jsonWebTokenService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final NotificationService notificationService;

    @Override
    public SignInResponse signIn(String username) {
        return new SignInResponse(
                getUserIdByUsername(username),
                jsonWebTokenService.getAccessToken(username),
                jsonWebTokenService.getRefreshToken(username)
        );
    }

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        validatePasswords(createUserRequest);
        userService.createUser(createUserRequest.username(), createUserRequest.password(), createUserRequest.businessName());
    }

    @Override
    public RestorePasswordResponse restorePassword(RestorePasswordRequest restorePasswordRequest) {
        try {
            User user = userService.loadUserByUsername(restorePasswordRequest.username());
            notificationService.notifyRestorePassword(
                    user.getEmail(),
                    user.getUsername(),
                    getGenerateRestorePasswordCode(user)
            );
            return new RestorePasswordResponse(user.getEmail());
        } catch (Exception e) {
            log.error("Error restoring password for user {}: {}", restorePasswordRequest.username(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void confirmRecoverPassword(RecoverPasswordRequest recoverPasswordRequest) {
        userService.recoverPassword(recoverPasswordRequest);
    }

    @Override
    public void validateToken(ValidateTokenRequest request) {
        this.passwordRecoveryTokenService.validateToken(request);
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        String subject = getSubject(refreshToken);
        return new SignInResponse(
                getUserIdByUsername(subject),
                jsonWebTokenService.getAccessToken(subject),
                jsonWebTokenService.getRefreshToken(subject)
        );
    }

    private UUID getUserIdByUsername(String refreshToken) {
        return userService.loadUserByUsername(
                refreshToken
        ).getId();
    }

    private String getSubject(String refreshToken) {
        return jsonWebTokenService.getSubject(refreshToken);
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jsonWebTokenService.isValid(refreshToken)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Refresh token is invalid");
        }
    }

    private String getGenerateRestorePasswordCode(User user) {
        return userService.generateRestorePasswordCode(user.getUsername());
    }

    private void validatePasswords(CreateUserRequest createUserRequest) {
        if(!passwordsMatch(createUserRequest)) {
            throw new ResponseStatusException(BAD_REQUEST, "Las contraseñas no coinciden");
        }
    }

    private boolean passwordsMatch(CreateUserRequest createUserRequest) {
        return createUserRequest.password().equals(createUserRequest.repeatedPassword());
    }

}
