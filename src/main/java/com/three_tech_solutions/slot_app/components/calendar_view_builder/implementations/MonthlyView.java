package com.three_tech_solutions.slot_app.components.calendar_view_builder.implementations;

import com.three_tech_solutions.slot_app.components.calendar_view_builder.CalendarViewBuilder;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class MonthlyView extends CalendarViewBuilder {
    public MonthlyView(SpecificSlotService specificSlotService) {
        super(specificSlotService);
    }

    @Override
    protected LocalDate getStartDate(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    @Override
    protected LocalDate getEndDate(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
}
