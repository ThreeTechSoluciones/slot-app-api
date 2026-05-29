package com.three_tech_solutions.slot_app.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    private DateUtils() {

    }

    public static LocalDate getNextOrSameDateOfDayOfWeek(DayOfWeek dayOfWeek) {
        return LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    public static LocalDate getNextDateWithSameDayOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.next(date.getDayOfWeek()));
    }

    public static String translateDay(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
