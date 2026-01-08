package com.three_tech_solutions.slot_app.data.mappers;

import com.three_tech_solutions.slot_app.controllers.requests.UpdateSlotRequest;
import com.three_tech_solutions.slot_app.controllers.responses.UserPreferencesResponse;
import com.three_tech_solutions.slot_app.controllers.responses.UserSlotResponse;
import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.User;
import org.springframework.stereotype.Component;
@Component
public class UserPreferencesMapper {
        public UserPreferencesResponse toUserPreferencesResponse(User user) {
            return new UserPreferencesResponse(
                    user.getUserPreferences().getSlotCapacity(),
                    user.getUserPreferences().getSlotDurationMinutes()
            );
        }
    }


