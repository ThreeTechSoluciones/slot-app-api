package com.three_tech_solutions.slot_app.cron_jobs.components;

import com.three_tech_solutions.slot_app.cron_jobs.data.enums.CronJobType;
import com.three_tech_solutions.slot_app.cron_jobs.data.models.CronJobAuditory;
import com.three_tech_solutions.slot_app.cron_jobs.services.interfaces.CronJobAuditoryService;
import com.three_tech_solutions.slot_app.data.models.Slot;
import com.three_tech_solutions.slot_app.data.models.User;
import com.three_tech_solutions.slot_app.services.interfaces.UserService;
import com.three_tech_solutions.slot_app.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.three_tech_solutions.slot_app.constants.SlotConstants.MONTHS_AHEAD;

@Service
@Slf4j
@AllArgsConstructor
public class CreateUpcomingSpecificSlots {

    private final CronJobAuditoryService cronJobAuditoryService;
    private final UserService userService;

    @Transactional
    @Scheduled(cron = "0 0 1 1 * *")
    void createUpcomingSpecificSlots() {
        log.info("Iniciando proceso de creación de slots específicos próximos");
        CronJobAuditory cronJobAuditory = cronJobAuditoryService.createCronJobExecution(CronJobType.CREATE_UPCOMING_SPECIFIC_SLOTS);
        try {
            List<User> users = userService.getUsers();
            users.forEach(user -> {
                List<Slot> userSlots = user.getSlots();
                userSlots.forEach(slot -> {
                    LocalDate startDate = DateUtils.getNextDateWithSameDayOfWeek(slot.getLastSpecificSlotDate());
                    LocalDate endDate = LocalDate.now().plusMonths(MONTHS_AHEAD).with(TemporalAdjusters.lastDayOfMonth());

                    slot.addSpecificSlots(
                            user.getUserPreferences().getSlotDurationMinutes(),
                            user.getUserPreferences().getSlotCapacity(),
                            user,
                            startDate,
                            endDate
                    );

                });
                userService.saveUser(user);
            });
            cronJobAuditoryService.setCronJobExecutionSuccess(cronJobAuditory);
        } catch (Exception e) {
            log.error("Hubo un error al crear los slots específicos", e);
            cronJobAuditoryService.setCronJobExecutionFailure(cronJobAuditory, e.getMessage());
            throw e;
        }
    }
}
