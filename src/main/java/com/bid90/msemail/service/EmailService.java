package com.bid90.msemail.service;

import com.bid90.msemail.dto.EmailRequest;
import com.bid90.msemail.exceptions.EmailException;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String body) throws EmailException;

    void sendTextEmail(String to, String subject, String text);

    void sendEmail(String to, String subject, String body, boolean enableHtml);

    void sendEmailToQueue(EmailRequest request);

}
