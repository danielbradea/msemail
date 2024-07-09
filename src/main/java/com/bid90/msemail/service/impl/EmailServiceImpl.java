package com.bid90.msemail.service.impl;

import com.bid90.msemail.config.RabbitMQConfig;
import com.bid90.msemail.dto.EmailRequest;
import com.bid90.msemail.exceptions.EmailException;
import com.bid90.msemail.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = RabbitMQConfig.QUEUE_NAME;
    private final String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender, RabbitTemplate rabbitTemplate,
                            @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.rabbitTemplate = rabbitTemplate;
        this.fromEmail = fromEmail;
    }

    /**
     * Sends an email with the specified recipient, subject, and HTML body.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param body    The HTML body of the email.
     * @throws EmailException If an exception occurs while sending the email.
     */
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(body, true);
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }

    }

    /**
     * Sends a text email with the specified recipient, subject, and text body.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param text    The text body of the email.
     * @throws EmailException If an exception occurs while sending the email.
     */
    public void sendTextEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(fromEmail);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    /**
     * Sends an email based on the specified parameters.
     *
     * @param to        The recipient's email address.
     * @param subject   The subject of the email.
     * @param body      The body content of the email.
     * @param enableHtml    Indicates whether the email body should be treated as HTML content.
     *                      If {@code true}, sends an HTML email; otherwise, sends a plain text email.
     */
    public void sendEmail(String to, String subject, String body, boolean enableHtml){
        if (!EmailValidator.getInstance().isValid(to)) {
            throw new IllegalArgumentException("Invalid email address: " + to);
        }
        if(enableHtml){
            sendHtmlEmail(to,subject,body);
        }else {
            sendTextEmail(to,subject,body);
        }
    }


    /**
     * Sends an email request to a RabbitMQ queue.
     *
     * @param request The email request containing details like recipient, subject, body, and HTML flag.
     */
    public void sendEmailToQueue(EmailRequest request) {
        if (!EmailValidator.getInstance().isValid(request.getTo())) {
            logger.error("Invalid email address:"+request.getTo());
            return;
        }
        rabbitTemplate.convertAndSend(QUEUE_NAME, request);
        logger.info("Email sent to RabbitMQ for processing: {}", request.getTo());
    }

}