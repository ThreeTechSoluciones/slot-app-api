package com.three_tech_solutions.slot_app.components.calendar_view_builder;

import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;

import java.time.LocalDate;
import java.util.List;

import static com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse.SpecificSlotResponseStatus.calculateStatus;

public abstract class CalendarViewBuilder {

    private final SpecificSlotService specificSlotService;

    public CalendarViewBuilder(SpecificSlotService specificSlotService) {
        this.specificSlotService = specificSlotService;
    }

    public CalendarResponse getCalendarView(User user, LocalDate date) {
        List<SpecificSlot> specificSlots = getSpecificSlots(user, date);
        List<CalendarResponse.Day> days = getDaysWhereUserHasAtLeastOneSlot(specificSlots);
        List<CalendarResponse.SlotTime> times = getTimesWhereUserHasAtLeastOneSlot(specificSlots);
        SpecificSlotResponse[][] calendar = buildCalendarWithSlotsByDayAndTime(times, days, specificSlots);

        return new CalendarResponse(
                days,
                times,
                calendar
        );
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

    private static SpecificSlotResponse buildSpecificSlotResponse(SpecificSlot specificSlot) {
        return new SpecificSlotResponse(
                specificSlot.getId(),
                specificSlot.getStartTime(),
                specificSlot.getEndTime(),
                specificSlot.getCapacity(),
                getSpecificSlotUsedCapacity(specificSlot),
                calculateStatus(specificSlot),
                getStudentsList(specificSlot)
        );
    }

    private static int getSpecificSlotUsedCapacity(SpecificSlot specificSlot) {
        return getStudentsWithAttendanceOrRecoveredStatus(specificSlot).size();
    }

    private static List<SpecificSlotDetail> getStudentsWithAttendanceOrRecoveredStatus(SpecificSlot specificSlot) {
        return specificSlot.getSpecificSlotDetails().stream().filter(SpecificSlotDetail::studentGoesToSlot).toList();
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

    private static SpecificSlotResponse[][] buildCalendarWithSlotsByDayAndTime(List<CalendarResponse.SlotTime> times, List<CalendarResponse.Day> days, List<SpecificSlot> specificSlots) {
        SpecificSlotResponse[][] calendar = new SpecificSlotResponse[times.size()][days.size()];
        for (SpecificSlot specificSlot : specificSlots) {
            int dayIndex = getDayIndex(specificSlot, days);
            int timeIndex = getTimeIndex(specificSlot, times);

            calendar[timeIndex][dayIndex] = buildSpecificSlotResponse(specificSlot);
        }
        return calendar;
    }

    private static int getTimeIndex(SpecificSlot specificSlot, List<CalendarResponse.SlotTime> times) {
        return times.indexOf(new CalendarResponse.SlotTime(
                specificSlot.getStartTime(),
                specificSlot.getEndTime()
        ));
    }

    private static int getDayIndex(SpecificSlot specificSlot, List<CalendarResponse.Day> days) {
        return days.indexOf(new CalendarResponse.Day(
                specificSlot.getSlotDate().getDayOfWeek(),
                specificSlot.getSlotDate().getDayOfMonth()
        ));
    }

    private static List<CalendarResponse.SlotTime> getTimesWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new CalendarResponse.SlotTime(
                        specificSlot.getStartTime(),
                        specificSlot.getEndTime()
                ))
                .distinct().toList();
    }

    private static List<CalendarResponse.Day> getDaysWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new CalendarResponse.Day(
                        specificSlot.getSlotDate().getDayOfWeek(),
                        specificSlot.getSlotDate().getDayOfMonth()
                ))
                .distinct().toList();
    }

}
