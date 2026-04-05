package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.controllers.requests.CreateUserRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RecoverPasswordRequest;
import com.three_tech_solutions.slot_app.controllers.requests.RestorePasswordRequest;
import com.three_tech_solutions.slot_app.controllers.responses.SignInResponse;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.AuthService;
import com.three_tech_solutions.slot_app.services.interfaces.JsonWebTokenService;
import com.three_tech_solutions.slot_app.services.interfaces.MailSenderService;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import com.three_tech_solutions.slot_app.utils.EmailUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@AllArgsConstructor
class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final JsonWebTokenService jsonWebTokenService;

    @Override
    public SignInResponse signIn(String username) {
        return new SignInResponse(
                userService.loadUserByUsername(username).getId(),
                jsonWebTokenService.getAccessToken(username),
                jsonWebTokenService.getRefreshToken(username)
        );
    }

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        validatePasswords(createUserRequest);
        userService.createUser(createUserRequest.username(), createUserRequest.password());
    }

    @Override
    public void restorePassword(RestorePasswordRequest restorePasswordRequest) {
        User user = userService.loadUserByUsername(restorePasswordRequest.username());
        mailSenderService.sendHtmlMessage(
            user.getEmail(),
            EmailUtils.RESTORE_PASSWORD_SUBJECT,
            EmailUtils.getRestorePasswordEmailContent(
                    user.getUsername(),
                    getGenerateRestorePasswordCode(user)
            )
        );
    }

    @Override
    public void confirmRecoverPassword(RecoverPasswordRequest recoverPasswordRequest) {
        userService.recoverPassword(recoverPasswordRequest);
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
