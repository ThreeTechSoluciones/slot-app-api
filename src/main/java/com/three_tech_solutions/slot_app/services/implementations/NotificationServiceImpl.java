package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.notifications.NotificationContentBuilder;
import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;
import com.three_tech_solutions.slot_app.services.interfaces.MailSenderService;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import com.three_tech_solutions.slot_app.utils.EmailUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MailSenderService mailSenderService;

    @Override
    public void notifyNewMonthlyFee(Student student, MonthlyFee monthlyFee) {
        String subject = "Nueva cuota generada";

        String message = NotificationContentBuilder.buildNewMonthlyFeeMessage(
                student,
                monthlyFee
        );

        String html = EmailUtils.wrapWithTemplate(subject, message);
        mailSenderService.sendHtmlMessage(student.getEmail(), subject, html);
    }
}