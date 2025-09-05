package com.blackcompany.eeos.calendar.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record CalendarCreateCommand(String title, String url, String type, Long startAt, Long endAt)
		implements AbstractResponseDto {}
