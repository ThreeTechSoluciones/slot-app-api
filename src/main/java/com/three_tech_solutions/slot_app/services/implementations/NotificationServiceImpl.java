package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.components.notifications.NotificationContentBuilder;
import com.three_tech_solutions.slot_app.controllers.responses.StudentSlotResponse;
import com.three_tech_solutions.slot_app.data.enums.NotificationStatus;
import com.three_tech_solutions.slot_app.data.enums.NotificationType;
import com.three_tech_solutions.slot_app.data.models.*;
import com.three_tech_solutions.slot_app.data.repositories.NotificationRepository;
import com.three_tech_solutions.slot_app.services.interfaces.MailSenderService;
import com.three_tech_solutions.slot_app.services.interfaces.NotificationService;
import com.three_tech_solutions.slot_app.utils.EmailUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final MailSenderService mailSenderService;
    private final NotificationRepository notificationRepository;

    @Override
    public void notifyWelcome(Student student, List<StudentSlotResponse> slots) {
        String message = NotificationContentBuilder.buildWelcomeMessage(student, student.getUser().getBusinessName(), slots);
        send(student.getEmail(), message, NotificationType.WELCOME, student.getUser());
    }

    @Override
    public void notifyRestorePassword(String email, String username, String code) {
        String message = NotificationContentBuilder.buildRestorePasswordMessage(username, code);
        send(email, message, NotificationType.RESTORE_PASSWORD,null);
    }

    @Override
    public void notifyNewMonthlyFee(Student student, MonthlyFee monthlyFee) {
        String message = NotificationContentBuilder.buildNewMonthlyFeeMessage(student, monthlyFee, student.getUser().getBusinessName());
        send(student.getEmail(), message, NotificationType.NEW_MONTHLY_FEE, student.getUser());
    }

    @Override
    public void notifyMonthlyFeeExpiration(MonthlyFee monthlyFee){
        Student student = monthlyFee.getStudent();
        String message = NotificationContentBuilder.buildMonthlyFeeExpirationMessage(student, monthlyFee, student.getUser().getBusinessName());
        send(student.getEmail(), message, NotificationType.MONTHLY_FEE_EXPIRATION, student.getUser());
    }

    @Override
    public void notifySlotRecovery(Student student, SpecificSlot specificSlot) {
        String message = NotificationContentBuilder.buildSlotRecoveryMessage(student, specificSlot, student.getUser().getBusinessName());
        send(student.getEmail(), message, NotificationType.SLOT_RECOVERY, student.getUser());
    }

    @Override
    public void notifySlotCanceled(Student student, LocalDate date, LocalTime startTime, boolean hasRecovery) {
        String message = NotificationContentBuilder.buildSlotCanceledMessage(student, student.getUser().getBusinessName(), date, startTime, hasRecovery);
        send(student.getEmail(), message, NotificationType.SPECIFIC_SLOT_CANCELED, student.getUser());
    }

    @Override
    public void notifyStudentAbsenceForSpecificSlot(Student student, SpecificSlot specificSlot) {
        String message = NotificationContentBuilder.buildStudentAbsenceForSpecificSlotMessage(student, specificSlot, student.getUser().getBusinessName());
        send(student.getEmail(), message, NotificationType.REGISTER_STUDENT_ABSENCE, student.getUser());
    }

    @Override
    public void notifyMonthlyFeeExpiringSoon(MonthlyFee monthlyFee) {
        Student student = monthlyFee.getStudent();
        String message = NotificationContentBuilder.buildMonthlyFeeExpiringSoonMessage(student, monthlyFee, student.getUser().getBusinessName());
        send(student.getEmail(), message, NotificationType.MONTHLY_FEE_EXPIRING_SOON, student.getUser());
    }

    private void send(String to, String message, NotificationType type, User user) {
        try {
            String subject = type.getSubject();
            String html = EmailUtils.formatEmail(subject, message);
            mailSenderService.sendHtmlMessage(to, subject, html);
            saveNotification(message, type, user, NotificationStatus.SENDED);
        } catch (Exception e) {
            // We log the error but we don't throw it because we don't want the failure of the notification to affect the main flow of the application.
            // For example, if we fail to send a notification about a new monthly fee, we don't want that to prevent the creation of the monthly fee.
            // The same applies to all other notifications.
            // We also save the notification in the database even if we fail to send the email, so at least we have a record of the notification attempt.
            // This way we can later analyze the failed notifications and try to resend them if necessary.
            log.error("Error sending notification: " + e.getMessage());
            saveNotification(message, type, user, NotificationStatus.FAILED);
        }
    }

    private void saveNotification(String message, NotificationType type, User user, NotificationStatus status) {

        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setSendDate(LocalDate.now());
        notification.setMessage(message);
        notification.setType(type);
        notification.setUser(user);
        notification.setStatus(status);

        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            // We log the error but we don't throw it because we don't want the failure of saving the notification to affect the main flow of the application.
            // For example, if we fail to save a notification about a new monthly fee, we don't want that to prevent the creation of the monthly fee.
            // The same applies to all other notifications.
            log.error("Error saving notification: " + e.getMessage());
        }
    }
}
