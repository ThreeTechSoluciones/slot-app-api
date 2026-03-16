package com.three_tech_solutions.slot_app.security.providers;

import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.security.authentications.BearerTokenAuthentication;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BearerAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = (User) userService.loadUserByUsername(getUsername(authentication));
        return BearerTokenAuthentication.authenticated(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                user.getAuthorities()
        );
    }

    private String getUsername(Authentication authentication) {
        return authentication.getPrincipal().toString();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
