package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.ListUserSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    List<StudentResponse> getUserStudents(UUID userId, String filter);

    User getUserByIdOrThrowException(UUID id) ;

    void createUser(String username, String password);

    List<PlanResponse> getUserPlans(UUID userId);

    ListUserSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek);
}
