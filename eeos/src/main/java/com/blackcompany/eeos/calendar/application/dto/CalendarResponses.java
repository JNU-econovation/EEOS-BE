package com.blackcompany.eeos.calendar.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.util.List;

public record CalendarResponses(List<CalendarResponse> calendars)
        implements AbstractResponseDto {

}
