package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> getById(UUID id);
}
