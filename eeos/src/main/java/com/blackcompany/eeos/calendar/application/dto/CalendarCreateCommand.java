package com.blackcompany.eeos.calendar.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.time.LocalDateTime;

public record CalendarCreateCommand(
        String title,
        String url,
        String type,
        LocalDateTime startAt,
        LocalDateTime endAt
) implements AbstractResponseDto {
}
