package com.three_tech_solutions.slot_app.services.interfaces;

public interface MailSenderService {

    void sendHtmlMessage(String to, String subject, String text);
}
