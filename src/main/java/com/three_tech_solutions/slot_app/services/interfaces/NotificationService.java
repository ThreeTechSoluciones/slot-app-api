package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.time.LocalDate;
import java.time.LocalTime;

public interface NotificationService {

    void notifyRestorePassword(String email, String username, String code);

    void notifyNewMonthlyFee(Student student, MonthlyFee monthlyFee);

    void notifyMonthlyFeeExpiration(MonthlyFee monthlyFee);

    void notifySlotCanceled(Student student, LocalDate date, LocalTime startTime, boolean hasRecovery);
}
