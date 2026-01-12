package com.three_tech_solutions.slot_app.components.calendar_view_builder.factory;

import com.three_tech_solutions.slot_app.components.calendar_view_builder.CalendarViewBuilder;
import com.three_tech_solutions.slot_app.components.calendar_view_builder.implementations.DailyView;
import com.three_tech_solutions.slot_app.components.calendar_view_builder.implementations.MonthlyView;
import com.three_tech_solutions.slot_app.components.calendar_view_builder.implementations.WeeklyView;
import com.three_tech_solutions.slot_app.data.enums.CalendarViewType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CalendarViewFactory {

    private final Map<CalendarViewType, CalendarViewBuilder> calendarViews;

    public CalendarViewFactory(
            MonthlyView monthlyView,
            WeeklyView weeklyView,
            DailyView dailyView
    ) {
        this.calendarViews = Map.of(
                CalendarViewType.MONTHLY, monthlyView,
                CalendarViewType.WEEKLY, weeklyView,
                CalendarViewType.DAILY, dailyView
        );
    }

    public CalendarViewBuilder getCalendarViewBuilder(CalendarViewType viewType){
        return calendarViews.get(viewType);
    }
}
