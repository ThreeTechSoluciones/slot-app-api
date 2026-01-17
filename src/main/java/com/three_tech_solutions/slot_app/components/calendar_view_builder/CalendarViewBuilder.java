package com.three_tech_solutions.slot_app.components.calendar_view_builder;

import com.three_tech_solutions.slot_app.controllers.responses.NewCalendarResponse;
import com.three_tech_solutions.slot_app.controllers.responses.SpecificSlotResponse;
import com.three_tech_solutions.slot_app.data.enums.SpecificSlotStatus;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.SpecificSlotDetail;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.SpecificSlotService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class CalendarViewBuilder {

    private final SpecificSlotService specificSlotService;

    public CalendarViewBuilder(SpecificSlotService specificSlotService) {
        this.specificSlotService = specificSlotService;
    }

    public NewCalendarResponse getCalendarView(User user, LocalDate date) {
        List<SpecificSlot> specificSlots = getSpecificSlots(user, date);
        List<NewCalendarResponse.Day> days = getDaysWhereUserHasAtLeastOneSlot(specificSlots);
        List<NewCalendarResponse.SlotTime> times = getTimesWhereUserHasAtLeastOneSlot(specificSlots);
        SpecificSlotResponse[][] calendar = buildCalendarWithSlotsByDayAndTime(times, days, specificSlots);

        return new NewCalendarResponse(
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

    private static SpecificSlotResponse.SpecificSlotResponseStatus calculateStatus(SpecificSlot specificSlot) {
        if (specificSlot.getStatus().equals(SpecificSlotStatus.CANCELED)) {
            return SpecificSlotResponse.SpecificSlotResponseStatus.CANCELED;
        };

        if (specificSlot.getSlotDate().isBefore(LocalDate.now())) {
            return SpecificSlotResponse.SpecificSlotResponseStatus.FINALIZED;
        }

        if (
                specificSlot.getSlotDate().isEqual(LocalDate.now()) && (
                    specificSlot.getStartTime().isBefore(LocalTime.now()) && specificSlot.getEndTime().isAfter(LocalTime.now())
                )
        ) {
            return SpecificSlotResponse.SpecificSlotResponseStatus.IN_PROGRESS;
        }

        return SpecificSlotResponse.SpecificSlotResponseStatus.FUTURE;
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

    private static SpecificSlotResponse[][] buildCalendarWithSlotsByDayAndTime(List<NewCalendarResponse.SlotTime> times, List<NewCalendarResponse.Day> days, List<SpecificSlot> specificSlots) {
        SpecificSlotResponse[][] calendar = new SpecificSlotResponse[times.size()][days.size()];
        for (SpecificSlot specificSlot : specificSlots) {
            int dayIndex = getDayIndex(specificSlot, days);
            int timeIndex = getTimeIndex(specificSlot, times);

            calendar[timeIndex][dayIndex] = buildSpecificSlotResponse(specificSlot);
        }
        return calendar;
    }

    private static int getTimeIndex(SpecificSlot specificSlot, List<NewCalendarResponse.SlotTime> times) {
        return times.indexOf(new NewCalendarResponse.SlotTime(
                specificSlot.getStartTime(),
                specificSlot.getEndTime()
        ));
    }

    private static int getDayIndex(SpecificSlot specificSlot, List<NewCalendarResponse.Day> days) {
        return days.indexOf(new NewCalendarResponse.Day(
                specificSlot.getSlotDate().getDayOfWeek(),
                specificSlot.getSlotDate().getDayOfMonth()
        ));
    }

    private static List<NewCalendarResponse.SlotTime> getTimesWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new NewCalendarResponse.SlotTime(
                        specificSlot.getStartTime(),
                        specificSlot.getEndTime()
                ))
                .distinct().toList();
    }

    private static List<NewCalendarResponse.Day> getDaysWhereUserHasAtLeastOneSlot(List<SpecificSlot> specificSlots) {
        return specificSlots.stream().map(specificSlot -> new NewCalendarResponse.Day(
                        specificSlot.getSlotDate().getDayOfWeek(),
                        specificSlot.getSlotDate().getDayOfMonth()
                ))
                .distinct().toList();
    }

}
