package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import com.three_tech_solutions.slot_app.data.models.User;

import java.time.LocalDate;
import java.util.List;

public interface CalendarService {
    List<CalendarResponse> getUserCalendar(User user, CalendarViewType viewType, LocalDate date);
}
