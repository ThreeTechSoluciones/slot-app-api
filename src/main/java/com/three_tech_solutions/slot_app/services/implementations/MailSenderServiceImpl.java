package com.three_tech_solutions.slot_app.services.implementations;


import com.three_tech_solutions.slot_app.services.interfaces.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "services.email",
        name = "active",
        havingValue = "true"
)
public class MailSenderServiceImpl implements MailSenderService {

    private JavaMailSenderImpl mailSender;

    @Override
    public void sendHtmlMessage(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

}
