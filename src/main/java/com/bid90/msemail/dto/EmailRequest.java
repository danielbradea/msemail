package com.bid90.msemail.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class EmailRequest {
    String to;
    String subject;
    String body;
    private boolean enableHtml;
}
