package com.three_tech_solutions.slot_app.services.implementations;

import com.three_tech_solutions.slot_app.services.interfaces.MailSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "services.email",
        name = "active",
        havingValue = "false"
)
public class DummyMailSenderServiceImpl implements MailSenderService {
    @Override
    public void sendHtmlMessage(String to, String subject, String text) {
        log.info("Dummy email sent to: {}, subject: {}, text: {}", to, subject, text);
    }
}
