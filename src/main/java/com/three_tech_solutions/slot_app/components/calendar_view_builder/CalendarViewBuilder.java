package com.three_tech_solutions.slot_app.components.calendar_view_builder;

import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CalendarViewBuilder {

    private final SpecificSlotService specificSlotService;

    public CalendarViewBuilder(SpecificSlotService specificSlotService) {
        this.specificSlotService = specificSlotService;
    }

    public List<CalendarResponse> getCalendarView(User user, LocalDate date) {
        List<CalendarResponse> calendarResponses = new ArrayList<>();

        getSpecificSlots(user, date)
                .stream()
                .collect(Collectors.groupingBy(SpecificSlot::getSlotDate))
                .forEach((dateKey, specificSlot) -> calendarResponses.add(
                        buildCalendarResponse(specificSlot, dateKey)
                ));

        return calendarResponses.stream().sorted(Comparator.comparingInt(CalendarResponse::numberOfDay)).toList();
    }

    protected abstract LocalDate getStartDate(LocalDate date);

    protected abstract LocalDate getEndDate(LocalDate date);

    private List<SpecificSlot> getSpecificSlots(User user, LocalDate date) {
        return specificSlotService.getAllByUserAndDateBetween(
                user,
                getStartDate(date),
                getEndDate(date)
        );
    }

    private CalendarResponse buildCalendarResponse(List<SpecificSlot> specificSlots, LocalDate date) {
        return new CalendarResponse(
                date.getDayOfWeek(),
                date.getDayOfMonth(),
                specificSlots
                        .stream()
                        .map(CalendarViewBuilder::buildSpecificSlotResponse)
                        .toList()
        );
    }

    private static SpecificSlotResponse buildSpecificSlotResponse(SpecificSlot specificSlot) {
        return new SpecificSlotResponse(
                specificSlot.getId(),
                specificSlot.getStartTime(),
                specificSlot.getEndTime(),
                specificSlot.getCapacity(),
                specificSlot.getSpecificSlotDetails().size(),
                getStudentsList(specificSlot)
        );
    }

    private static List<SpecificSlotResponse.Student> getStudentsList(SpecificSlot specificSlot) {
        return specificSlot.getSpecificSlotDetails().stream().map(CalendarViewBuilder::buildStudentResponse).toList();
    }

    private static SpecificSlotResponse.Student buildStudentResponse(SpecificSlotDetail specificSlotDetail) {
        return new SpecificSlotResponse.Student(
                specificSlotDetail.getStudent().getId(),
                specificSlotDetail.getStudent().getName() + " " + specificSlotDetail.getStudent().getLastname(),
                specificSlotDetail.getStatus()
        );
    }

}
