package com.bid90.msemail.config;

import com.bid90.msemail.dto.EmailRequest;
import com.bid90.msemail.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailReceiver {

    private static final Logger logger = LoggerFactory.getLogger(EmailReceiver.class);
    private final EmailService emailService;

    public EmailReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    public void receiveMessage(EmailRequest request) {
        logger.info("Received a new email request.");
        emailService.sendEmail(request.getTo(),request.getSubject(),request.getBody(),request.isEnableHtml());
    }
}