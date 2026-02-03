package com.three_tech_solutions.slot_app.components.calendar_view_builder;

import com.three_tech_solutions.slot_app.controllers.responses.CalendarResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
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
                sortedDays(days),
                sortedTimes(times),
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

    private int getSpecificSlotUsedCapacity(SpecificSlot specificSlot) {
        return slotService.getSpecificSlotUsedCapacity(specificSlot);
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
                specificSlot.getEndTime(),
                isCurrentTime(specificSlot.getStartTime(), specificSlot.getEndTime(), specificSlot.getSlotDate())
        ));
    }

    private static int getDayIndex(SpecificSlot specificSlot, List<CalendarResponse.Day> days) {
        return days.indexOf(new CalendarResponse.Day(
                specificSlot.getSlotDate().getDayOfWeek(),
                specificSlot.getSlotDate().getDayOfMonth(),
                isCurrentDay(specificSlot.getSlotDate().getDayOfWeek())
        ));
    }

    private static List<CalendarResponse.SlotTime> getTimesWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new CalendarResponse.SlotTime(
                        specificSlot.getStartTime(),
                        specificSlot.getEndTime(),
                        isCurrentTime(specificSlot.getStartTime(), specificSlot.getEndTime(), specificSlot.getSlotDate())
                ))
                .distinct().toList();
    }


    private static List<CalendarResponse.Day> getDaysWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new CalendarResponse.Day(
                        specificSlot.getSlotDate().getDayOfWeek(),
                        specificSlot.getSlotDate().getDayOfMonth(),
                        isCurrentDay(specificSlot.getSlotDate().getDayOfWeek())
                ))
                .distinct().toList();
    }

    /* Method to validate if the time of the slot is the current one, but we also check the day because
    * if we have a Slot on Monday from 22:30 to 23:30 and another Slot Thursday from 22:00 to 23:00
    * If we don't check the day, both of them are current times even if today its Monday at 22:40 */
    private static boolean isCurrentTime(LocalTime startTime, LocalTime endTime, LocalDate slotDate) {
        return LocalTime.now().isAfter(startTime) && LocalTime.now().isBefore(endTime) && isCurrentDay(slotDate.getDayOfWeek());
    }

    private static boolean isCurrentDay(DayOfWeek dayOfWeek) {
        return dayOfWeek == LocalDate.now().getDayOfWeek();
    }

    private static List<CalendarResponse.SlotTime> sortedTimes(List<CalendarResponse.SlotTime> times) {
        return times.stream().sorted(Comparator.comparing(CalendarResponse.SlotTime::startTime)).toList();
    }

    private static List<CalendarResponse.Day> sortedDays(List<CalendarResponse.Day> days) {
        return days.stream().sorted(Comparator.comparingInt(day -> day.dayOfWeek().getValue())).toList();
    }

}
