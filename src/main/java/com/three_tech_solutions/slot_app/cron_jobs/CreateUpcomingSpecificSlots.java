package com.three_tech_solutions.slot_app.cron_jobs;

import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import com.three_tech_solutions.slot_app.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CreateUpcomingSpecificSlots {

    private final UserService userService;

    @Transactional
    @Scheduled(cron = "* * * * * *")
    void createUpcomingSpecificSlots() {
        log.info("Iniciando proceso de creación de slots específicos próximos");
        List<User> users = userService.getUsers();
        users.forEach(user -> {
            try {
                List<Slot> userSlots = user.getSlots();
                userSlots.forEach(slot -> {
                    LocalDate startDate = DateUtils.getNextDateWithSameDayOfWeek(slot.getLastSpecificSlotDate());
                    LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

                    slot.addSpecificSlots(
                            user.getUserPreferences().getSlotDurationMinutes(),
                            user.getUserPreferences().getSlotCapacity(),
                            user,
                            startDate,
                            endDate
                    );

                });

                userService.saveUser(user);
            } catch (Exception e) {
                log.error("Hubo un error al crear los slots específicos para el usuario ", e);
            }
        });
    }
}
