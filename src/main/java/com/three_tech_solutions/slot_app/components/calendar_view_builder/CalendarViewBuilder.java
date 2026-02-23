package com.three_tech_solutions.slot_app.components.calendar_view_builder;

import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.mappers.StudentMapper;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse.SpecificSlotResponseStatus.calculateStatus;

public abstract class CalendarViewBuilder {

    private final SpecificSlotService specificSlotService;

    public CalendarViewBuilder(SpecificSlotService specificSlotService) {
        this.specificSlotService = specificSlotService;
    }

    public CalendarResponse getCalendarView(User user, LocalDate date) {
        List<SpecificSlot> specificSlots = getSpecificSlots(user, date);
        List<CalendarResponse.Day> days = sortedDays(getDaysWhereUserHasAtLeastOneSlot(specificSlots));
        List<CalendarResponse.SlotTime> times = sortedTimes(getTimesWhereUserHasAtLeastOneSlot(specificSlots));
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
                specificSlot.getSpecificSlotUsedCapacity(),
                calculateStatus(specificSlot),
                getStudentsList(specificSlot)
        );
    }

    private static List<SpecificSlotResponse.Student> getStudentsList(SpecificSlot specificSlot) {
        return specificSlot.getSpecificSlotDetails().stream().map(StudentMapper::buildStudentResponse).toList();
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
                specificSlot.getSlotDate().getDayOfMonth(),
                isCurrentDay(specificSlot.getSlotDate())
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
                        specificSlot.getSlotDate().getDayOfMonth(),
                        isCurrentDay(specificSlot.getSlotDate())
                ))
                .distinct().toList();
    }

    private static boolean isCurrentDay(LocalDate day) {
        return Objects.equals(day, LocalDate.now());
    }

    private static List<CalendarResponse.SlotTime> sortedTimes(List<CalendarResponse.SlotTime> times) {
        return times.stream().sorted(Comparator.comparing(CalendarResponse.SlotTime::startTime)).toList();
    }

    private static List<CalendarResponse.Day> sortedDays(List<CalendarResponse.Day> days) {
        return days.stream().sorted(Comparator.comparingInt(day -> day.dayOfWeek().getValue())).toList();
    }

}
