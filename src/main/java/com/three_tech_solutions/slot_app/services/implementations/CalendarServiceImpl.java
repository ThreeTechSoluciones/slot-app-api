package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.calendar_view_builder.factory.CalendarViewFactory;
import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.CalendarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarViewFactory calendarViewFactory;

    @Override
    public List<CalendarResponse> getUserCalendar(User user, CalendarViewType viewType, LocalDate date) {
        return calendarViewFactory
                .getCalendarViewBuilder(viewType)
                .getCalendarView(user, date);
    }

}
