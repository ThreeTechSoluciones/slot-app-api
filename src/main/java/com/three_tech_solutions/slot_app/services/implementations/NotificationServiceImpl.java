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
    public void notifyRestorePassword(String email, String username, String code) {
        String subject = "Restablecimiento de contraseña";

        String message = NotificationContentBuilder.buildRestorePasswordMessage(
                username,
                code
        );

        String html = EmailUtils.formatEmail(subject, message);
        mailSenderService.sendHtmlMessage(email, subject, html);
    }

    @Override
    public void notifyNewMonthlyFee(Student student, MonthlyFee monthlyFee) {
        String subject = "Nueva cuota generada";

        String businessName = student.getUser() != null
                ? student.getUser().getBusinessName()
                : null;

        String message = NotificationContentBuilder.buildNewMonthlyFeeMessage(
                student,
                monthlyFee,
                businessName
        );

        String html = EmailUtils.formatEmail(subject, message);
        mailSenderService.sendHtmlMessage(student.getEmail(), subject, html);
    }
}