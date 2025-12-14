package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateUserCapacityRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotsResponse;
import com.three_tech_solutions.slot_app.controllers.responses.PlanResponse;
import com.three_tech_solutions.slot_app.controllers.responses.StudentResponse;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    Page<StudentResponse> getUserStudents(UUID userId, String filter, Pageable pageable);

    User getUserByIdOrThrowException(UUID id) ;

    void createUser(String username, String password);

    List<PlanResponse> getUserPlans(UUID userId, String planName);

    void updateUserCapacityPreference(UUID userId, UpdateUserCapacityRequest updateUserCapacityRequest);

    UserSlotsResponse getSlotsByDayOfWeek(UUID userId, DayOfWeek dayOfWeek);
}
