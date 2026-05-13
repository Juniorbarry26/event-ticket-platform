package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AdminEmailTemplateDto {
    private UUID templateId;
    private String templateName;
    private String subject;
    private String body;
    private String category; // WELCOME, REMINDER, PROMOTIONAL, etc.
}
