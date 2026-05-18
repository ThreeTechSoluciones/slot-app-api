package com.three_tech_solutions.slot_app.services.interfaces;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Price;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.Student;

public interface NotificationService {

    void notifyRestorePassword(String email, String username, String code);

    void notifyNewMonthlyFee(Student student, MonthlyFee monthlyFee);

    void notifyMonthlyFeeExpiration(MonthlyFee monthlyFee);

    void notifySlotRecovery(Student student, SpecificSlot specificSlot);

    void notifyNewPrice(Student student, Price price);
}
