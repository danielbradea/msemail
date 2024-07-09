package com.bid90.msemail.dto;

import lombok.Data;


@Data
public class EmailRequest {
    String to;
    String subject;
    String body;
    private boolean enableHtml;
}
