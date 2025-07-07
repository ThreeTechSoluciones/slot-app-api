package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.User;
import java.util.UUID;

public interface UserService {
    User getUserByIdOrThrowException(UUID id) ;
}
